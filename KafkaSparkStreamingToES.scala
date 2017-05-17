package rnd

import kafka.serializer.StringDecoder
import org.apache.spark.sql.SQLContext
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object KafkaSparkStreamingToES {
  def main(args: Array[String]) {

    //All Configuration declar here
    val conf = new SparkConf()
      .setAppName("kafkawordcount")
      .setMaster("local")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    //Declare all the Configs here
    val kafkaTopics = "wordcounttopic"    // command separated list of topics
    val kafkaBrokers = "localhost:9092"   // comma separated list of broker:host
    val batchIntervalSeconds = 2
    val checkpointDir = "C://kafka_2.10-0.9.0.1/checkpoint/" //create a checkpoint directory to periodically persist the data

    //If any Spark Streaming Context is present, it kills and launches a new ssc
    val stopActiveContext = true
    if (stopActiveContext) {
      StreamingContext.getActive.foreach { _.stop(stopSparkContext = false) }
    }

    //Create Kafka Stream with the Required Broker and Topic
    def kafkaConsumeStream(ssc: StreamingContext): DStream[(String, String)] = {
      val topicsSet = kafkaTopics.split(",").toSet
      val kafkaParams = Map[String, String]("metadata.broker.list" -> kafkaBrokers)
      KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
        ssc, kafkaParams, topicsSet)
    }

    //Define a spark streaming context with batch interval of 2 seconds
    val ssc = new StreamingContext(sc, Seconds(batchIntervalSeconds))

    // Get the word stream from the Kafka source
    val wordStream = kafkaConsumeStream(ssc).flatMap { event => event._2.split(" ") }

    // Create a stream to do a running count of the words
    val updateFunc = (values: Seq[Int], state: Option[Int]) => {
      val currentCount = values.sum
      val previousCount = state.getOrElse(0)
      Some(currentCount + previousCount)
    }

    //for each words in the data stream count it as 1 for later grouping
    val runningCountStream = wordStream.map { x => (x, 1) }.updateStateByKey(updateFunc)

    // Create temp table at every batch interval
    runningCountStream.foreachRDD { rdd =>
      val sqlContext = SQLContext.getOrCreate(SparkContext.getOrCreate())
      val wordCountdf = sqlContext.createDataFrame(rdd).toDF("word", "count")
      wordCountdf.show()


      //Index the Word, Count attributes to ElasticSearch Index. You don't need to create any index in Elastic Search
      import org.elasticsearch.spark.sql._
      wordCountdf.saveToEs("kafkawordcount_v1/kwc")
    }

    // To make sure data is not deleted by the time we query it interactively
    ssc.remember(Minutes(1))
    ssc.checkpoint(checkpointDir)
    ssc
    //    }

    // This starts the streaming context in the background.
    ssc.start()

    // This is to ensure that we wait for some time before the background streaming job starts. This will put this cell on hold for 5 times the batchIntervalSeconds.
    ssc.awaitTerminationOrTimeout(batchIntervalSeconds * 5 * 1000)
  }
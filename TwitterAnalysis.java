package edu.utd.client;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.spark.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;
import scala.Tuple2;
import org.apache.spark.streaming.kafka.KafkaUtils;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.*;
import java.sql.Timestamp;
import org.apache.commons.configuration.Configuration;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.hadoop.cfg.ConfigurationOptions;
import org.elasticsearch.spark.streaming.api.java.JavaEsSparkStreaming;

public class TwitterAnalysis {
	public static void main(String[] args) throws InterruptedException {
		// Create a local StreamingContext with two working thread and batch
		// interval of 1 second
		SparkConf conf = new SparkConf().setMaster("local[4]").setAppName("KafkaConsumer");
		conf.set("spark.kryo.registrator", "MyKryoRegistrator");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));

		int numThreads = 4;
		Map<String, Integer> topicMap = new HashMap<>();
		topicMap.put("tweeterstream", numThreads);

		JavaPairReceiverInputDStream<String, String> messages = KafkaUtils.createStream(jssc, "localhost", "group1",
				topicMap);

		JavaDStream<String> lines = messages.map(Tuple2::_2);
		lines.print();
		JavaDStream<Tweet> tweets = lines.map(x -> createObject(x));
		JavaDStream<Tweet> tweetsWithSentiments = tweets.map(x -> performSentimentAnalysis(x));
		JavaDStream<String> twits = tweetsWithSentiments.map(x -> x.toString());
		JavaEsSparkStreaming.saveJsonToEs(twits, "bigdata/docs");
		twits.print();
		jssc.start();
		jssc.awaitTermination();
	}

	public static Tweet createObject(String s) {
		Gson gson = new Gson();
		Tweet t = new Tweet();
		t = gson.fromJson(s, Tweet.class);
		GeoPoint g= new GeoPoint();
		g.resetLat(t.getLatitude());
		g.resetLon(t.getLongitude());
		t.setLocation(g);
		System.out.println(t.getLocation().getLat() + " " + t.getLocation().getLon() );
	//	t.location = t.latitude + "," + t.longitude;
		
		t.setTimestamp(new Date().getTime());
		return t;
	}

	public static Tweet performSentimentAnalysis(Tweet t) {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		int mainSentiment = 0;
		if (t.getTweet() != null && t.getTweet().length() > 0) {
			int longest = 0;
			Annotation annotation = pipeline.process(t.getTweet());
			for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
				Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
				int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
				String partText = sentence.toString();
				if (partText.length() > longest) {
					mainSentiment = sentiment;
					longest = partText.length();
				}

			}
		}
		switch (mainSentiment) {
		case 2:
			t.setSentiment("NEUTRAL");
			break;
		case 1:
		case 0:
			t.setSentiment("NEGATIVE");
			break;
		case 3:
		case 4:
			t.setSentiment("POSITIVE");
			break;
		default:
			break;
		}
		return t;
	}
}

You are required to implement the following framework using Apache Spark Streaming, Kafka, Elasticsearch and Kibana. The framework performs SENTIMENT analysis of particular hash tags in tweetter data in real-time. For example, we want to do the sentiment analysis for all the tweets for #trump, #obama and show them (e.g., positive, neutral, negative, etc. tweets) on a map. When we show tweets on a map, we plot them using their latitude and longitude.  







Figure: Sentiment analysis framework

The above framework has the following components:

1.	Scrapper:
The scrapper will collect all tweets and sends them to Kafka for analytics. Please DONOT USE SPARK STREAMING TWEETTER API for collecting tweets. The scraper will be a standalone program written in JAVA/PYTHON and should perform the followings:
a.	Collecting tweets in real-time with particular hash tags. For example, we will collect all tweets with #trum, #obama.
b.	After getting tweets we will filter them based on their latitude and longitude. If any tweet does not have latitude and longitude, we will discard them.
c.	After filtering, we will send them (tweets with lat/lng) to Kafka.
d.	You should use Kafka API (producer) in your program (https://kafka.apache.org/090/documentation.html#producerapi)
e.	You scrapper program will run infinitely and should take hash tag as input parameter while running.

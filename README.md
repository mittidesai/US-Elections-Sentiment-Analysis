Implemented the following framework using Apache Spark Streaming, Kafka, Elasticsearch and Kibana.

The framework performs SENTIMENT analysis of particular hash tags from the twitter data in real-time.

For example, I did the sentiment analysis for all the tweets for #trump, #obama and show them (e.g., positive, neutral, negative, etc. tweets) on a map. While showing tweets on a map, I plotted them using their latitude and longitude.

Sentiment analysis framework:

Scrapper --> Kafka --> Sentiment Analyzer + Spark Streaming --> Visualizer (Elasticsearch + Kibana)

The above framework has the following components:

Scrapper: The scrapper collects all tweets and sends them to Kafka for analytics. The scraper uses a standalone program written in JAVA and performs the following: a.	Collects tweets in real-time with particular hash tags. For example, All tweets with #trump, #obama. b.	After getting tweets they are filtered based on their latitude and longitude. If any tweet does not have latitude and longitude, they are discarded. c.	After filtering, they're sent (tweets with lat/lng) to Kafka. d. Used Kafka API (producer) in this program (https://kafka.apache.org/090/documentation.html#producerapi) e.	The scrapper program runs infinitely and takes hash tag as input parameter while running.

Kafka Installed Kafka and run Kafka Server with Zookeeper. You should create a dedicated channel/topic for data transport.

Sentiment Analyzer Sentiment Analysis is the process of determining whether a piece of writing is positive, negative or neutral. It's also known as opinion mining, deriving the opinion or attitude of a speaker. For example, “President Donald Trump approaches his first big test this week from a position of unusual weakness.” - has positive sentiment. “Trump has the lowest standing in public opinion of any new president in modern history.” - has neutral sentiment. “Trump has displayed little interest in the policy itself, casting it as a thankless chore to be done before getting to tax-cut legislation he values more.” - has negative sentiment.

The above examples are taken from CNBC news: http://www.cnbc.com/2017/03/22/trumps-first-big-test-comes-as-hes-in-an-unusual-position-of-weakness.html

Used a third party sentiment analyzer like Stanford CoreNLP (java/scala), nltk(python) for sentiment analyzing. Added Stanford CoreNLP as an external library using SBT/Maven in my scala/java project.

Sentiment analysis using Spark Streaming: In Spark Streaming, created a Kafka consumer (shown in the class for streaming word count) and periodically collected filtered tweets from scrapper. For each has tag, performed sentiment analysis using Sentiment Analyzing tool (discussed above). Then for each hash tag, sent the output to Elasticsearch for visualization.

Visualizer Installed Elasticsearch and Kibana. Created an index for visualization. Created a map to show all kinds of tweets. After that, created a dashboard to show the map. Dashboard requires data to be time stamped. So, when data was sent from Spark to Elasticsearch added a time stamp. In the dashboard set a Map refresh time to 2 min as an example.
Tweets were shown for different categories. For example, if we have two hash tags, #obama and #trump, used different color icons for showing them in map. Used small circle with different colors for showing tweets.

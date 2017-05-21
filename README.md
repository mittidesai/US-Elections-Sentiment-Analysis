# US-Elections-Sentiment-Analysis
implement the following framework using Apache Spark Streaming, Kafka, Elasticsearch and Kibana. The framework performs SENTIMENT analysis of particular hash tags in tweetter data in real-time. For example, we want to do the sentiment analysis for all the tweets for #trump, #obama and show them (e.g., positive, neutral, negative, etc. tweets) on a map. When we show tweets on a map, we plot them using their latitude and longitude.  







Figure: Sentiment analysis framework

The above framework has the following components:

1.	Scrapper:
The scrapper will collect all tweets and sends them to Kafka for analytics. Please DONOT USE SPARK STREAMING TWEETTER API for collecting tweets. The scraper will be a standalone program written in JAVA/PYTHON and should perform the followings:
a.	Collecting tweets in real-time with particular hash tags. For example, we will collect all tweets with #trum, #obama.
b.	After getting tweets we will filter them based on their latitude and longitude. If any tweet does not have latitude and longitude, we will discard them.
c.	After filtering, we will send them (tweets with lat/lng) to Kafka.
d.	You should use Kafka API (producer) in your program (https://kafka.apache.org/090/documentation.html#producerapi)
e.	You scrapper program will run infinitely and should take hash tag as input parameter while running.
2.	Kafka
You need to install Kafka and run Kafka Server with Zookeeper. You should create a dedicated channel/topic for data transport.
3.	Sentiment Analyzer
Sentiment Analysis is the process of determining whether a piece of writing is positive, negative or neutral. It's also known as opinion mining, deriving the opinion or attitude of a speaker.
For example,
“President Donald Trump approaches his first big test this week from a position of unusual weakness.”  - has positive sentiment.
“Trump has the lowest standing in public opinion of any new president in modern history.” - has neutral sentiment.
“Trump has displayed little interest in the policy itself, casting it as a thankless chore to be done before getting to tax-cut legislation he values more.” - has negative sentiment.

The above examples are taken from CNBC news:
http://www.cnbc.com/2017/03/22/trumps-first-big-test-comes-as-hes-in-an-unusual-position-of-weakness.html

You can use any third party sentiment analyzer like Stanford CoreNLP (java/scala), nltk(python) for sentiment analyzing. For example, you can add Stanford CoreNLP as an external library using SBT/Maven  in your scala/java project. In python you can import nltk by installing it using pip.

Sentiment analysis using Spark Streaming:
In Spark Streaming, create a Kafka consumer (shown in the class for streaming word count) and periodically collect filtered tweets from scrapper. For each has tag, perform sentiment analysis using Sentiment Analyzing tool (discussed above). Then for each hash tag, send the output to Elasticsearch for visualization.



4.	Visualizer
Install Elasticsearch and Kibana. Create an index for visualization. Create a map to show all kinds of tweets. After that, create a dashboard to show the map. Dashboard requires data to be time stamped. So, when you send data from Spark to Elasticsearch add a time stamp. In the dashboard set a Map refresh time to 2 min as an example.

(OPTIONAL) – Bonus 5 point s will be given if you show tweets for different categories. For example, if we have two hash tags, #obama and #trump, we will use different color icons for showing them in map. 
You can use small circle with different colors for showing tweets.
ELK tutorial:
https://www.oreilly.com/learning/a-guide-to-elasticsearch-5-and-the-elkelastic-stack
https://www.digitalocean.com/community/tutorials/how-to-use-kibana-dashboards-and-visualizations
 
YOU SHOULD INSTALL ALL OF THESE IN YOUR MACHINE. CS6360 does not give you permission to install Elasticsearch, Kibana, etc.



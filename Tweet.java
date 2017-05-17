package edu.utd.client;

import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.index.fielddata.GeoPointValues;

public class Tweet {
	String tweet;
	double latitude;
	double longitude;
	GeoPoint location;
	String sentiment;
	long timestamp;
	
	public String getSentiment() {
		return sentiment;
	}
	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}
	public GeoPoint getLocation() {
		return location;
	}
	public void setLocation(GeoPoint location) {
		this.location = location;
	}
	public String getTweet() {
		return tweet;
	}
	public void setTweet(String tweet) {
		this.tweet = tweet;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return  tweet +":.:" + latitude
				+ ":.:" + longitude ;
	}
	
	
}

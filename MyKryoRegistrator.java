package edu.utd.client;

import java.io.Serializable;

import org.apache.spark.serializer.KryoRegistrator;

import com.esotericsoftware.kryo.Kryo;

public class MyKryoRegistrator implements KryoRegistrator, Serializable {


	@Override
	public void registerClasses(Kryo kryo) {
		 kryo.register(Tweet.class); 
		
	}
}

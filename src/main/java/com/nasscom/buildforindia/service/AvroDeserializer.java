package com.nasscom.buildforindia.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.nasscom.buildforindia.model.BabyDataScans;

public class AvroDeserializer implements Deserializer<BabyDataScans>{
    private String encoding = "UTF8";

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
        String propertyName = isKey ? "key.deserializer.encoding" : "value.deserializer.encoding";
        Object encodingValue = configs.get(propertyName);
        if (encodingValue == null)
            encodingValue = configs.get("deserializer.encoding");
        if (encodingValue != null && encodingValue instanceof String)
            encoding = (String) encodingValue;		
	}

	@Override
	public BabyDataScans deserialize(String topic, byte[] data) {
        try {
            if (data == null)
                return null;
            else
                return BabyDataScans.fromByteBuffer(ByteBuffer.wrap(data));
        } catch (IOException e) {
			e.printStackTrace();
            throw new SerializationException("Error when deserializing byte[] to string due to unsupported encoding " + encoding);
        }
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}

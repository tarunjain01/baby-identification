package com.nasscom.buildforindia.service;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import com.nasscom.buildforindia.model.BabyDataScans;

public class AvroSerializer implements Serializer<BabyDataScans>  {
    private String encoding = "UTF8";

    @Override
    public void close() {
        // nothing to do
    }

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		String propertyName = isKey ? "key.serializer.encoding" : "value.serializer.encoding";
        Object encodingValue = configs.get(propertyName);
        if (encodingValue == null)
            encodingValue = configs.get("serializer.encoding");
        if (encodingValue != null && encodingValue instanceof String)
            encoding = (String) encodingValue;
	}

	@Override
	public byte[] serialize(String topic, BabyDataScans data) {
        try {
            if (data == null)
                return null;
            else
                return data.toByteBuffer().array();
        } catch (IOException e) {
            throw new SerializationException("Error when serializing string to byte[] due to unsupported encoding " + encoding);
        }
	}
}

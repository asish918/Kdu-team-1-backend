package com.kdu.ibebackend.models.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.kdu.ibebackend.models.TenantConfig.Guests;

import java.io.IOException;

/**
 * Deserializer for nested DynamoDB object
 */
public class GuestsDeserializer extends JsonDeserializer<Guests> {

    @Override
    public Guests deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        Guests guests = new Guests();
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.currentName();
            jsonParser.nextToken(); // Move to value token

            switch (fieldName) {
                case "children":
                    guests.setChildren(jsonParser.getBooleanValue());
                    break;
                case "adults":
                    guests.setAdults(jsonParser.getBooleanValue());
                    break;
                case "teens":
                    guests.setTeens(jsonParser.getBooleanValue());
                    break;
            }
        }
        return guests;
    }
}

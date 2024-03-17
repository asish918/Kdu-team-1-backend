package com.kdu.ibebackend.models.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.kdu.ibebackend.models.TenantConfig.Guests;

import java.io.IOException;

/**
 * Serializer for nested DynamoDB object
 */
public class GuestsSerializer extends JsonSerializer<Guests> {

    @Override
    public void serialize(Guests guests, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeBooleanField("children", guests.isChildren());
        jsonGenerator.writeBooleanField("adults", guests.isAdults());
        jsonGenerator.writeBooleanField("teens", guests.isTeens());
        jsonGenerator.writeEndObject();
    }
}

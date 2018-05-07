package com.mobcrush.instagram.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;

public class StringEscapingSerializer extends StdSerializer<String> {

    public StringEscapingSerializer() {
        super(String.class);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeRawValue("\"" + StringEscapeUtils.escapeJava(value) + "\"");
    }
}

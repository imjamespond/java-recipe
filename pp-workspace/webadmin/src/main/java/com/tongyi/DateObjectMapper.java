package com.tongyi;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-10-21
 * Time: 下午4:36
 */
public class DateObjectMapper extends ObjectMapper {

    public DateObjectMapper(){
        CustomSerializerFactory factory = new CustomSerializerFactory();
        factory.addGenericMapping(Date.class, new JsonSerializer<Date>(){
            @Override
            public void serialize(Date value,
                                  JsonGenerator jsonGenerator,
                                  SerializerProvider provider)
                    throws IOException, JsonProcessingException {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                jsonGenerator.writeString(sdf.format(value));
            }
        });
        this.setSerializerFactory(factory);
    }
}
package com.test.qianxun;

import java.io.StringWriter;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.JsonNodeFactory;

import com.test.qianxun.util.JsonUtils;

public class JsonTest {

	public static void main(String[] args) {
		long list[] = new long[24];
		list[15] = 999;
		String json = JsonUtils.toJson(list);
		System.out.println(json);
		long[] jlist;
		try {
			jlist = JsonUtils.toObject(long[].class, "");
			System.out.println(jlist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end");
		
		
		

	    try {
			ObjectMapper mapper = new ObjectMapper();
			StringWriter sw = new StringWriter();  
			JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(sw);  

	        String[] arr = { "a", "b", "c" };
	        String str = "hello world jackson!";
	        /*
	        //byte
	        jsonGenerator.writeBinary(str.getBytes());
	        //boolean
	        jsonGenerator.writeBoolean(true);
	        //null
	        jsonGenerator.writeNull();
	        //float
	        jsonGenerator.writeNumber(2.2f);
	        //char
	        jsonGenerator.writeRaw("c");
	        //String
	        jsonGenerator.writeRaw(str, 5, 10);
	        //String
	        jsonGenerator.writeRawValue(str, 5, 5);
	        //String
	        jsonGenerator.writeString(str);
	        //jsonGenerator.writeTree(JsonNodeFactory.instance.POJONode(str));
	        jsonGenerator.close();
	        System.out.println(sw.toString());
	         */
	        

	        //Object
	        jsonGenerator.writeStartObject();//{
	        jsonGenerator.writeObjectFieldStart("user");//user:{
	        jsonGenerator.writeStringField("name", "jackson");//name:jackson
	        jsonGenerator.writeBooleanField("sex", true);//sex:true
	        jsonGenerator.writeNumberField("age", 22);//age:22
	        jsonGenerator.writeEndObject();//}      

	        jsonGenerator.writeArrayFieldStart("infos");//infos:[
	        jsonGenerator.writeNumber(22);//22
	        jsonGenerator.writeString("this is array");//this is array
	        jsonGenerator.writeEndArray();//]
	        
	        jsonGenerator.writeEndObject();//}	        

	        //complex Object
	        jsonGenerator.writeStartObject();//{
	        //jsonGenerator.writeObjectField("user", bean);//user:{bean}
	        //jsonGenerator.writeObjectField("infos", arr);//infos:[array]
	        jsonGenerator.writeEndObject();//}
	        jsonGenerator.close();
	        System.out.println(sw.toString());

	    } catch (Exception e) {

	        e.printStackTrace();

	    }
	}

}

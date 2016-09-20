package com.pengpeng.stargame.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.ASObject;
import flex.messaging.io.amf.Amf3Input;
import flex.messaging.io.amf.Amf3Output;

public class NetUtil {
	public static byte[] toByteArray(String cmd,Object data) throws IOException {
		SerializationContext serializationContext = new SerializationContext();
		Amf3Output amfOut = new Amf3Output(serializationContext);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		DataOutputStream dataOutStream = new DataOutputStream(outStream);
		amfOut.setOutputStream(dataOutStream);

		 Map<String, Object> map = new HashMap<String, Object>();
		 map.put("cmd", cmd);
		 map.put("data", data);

		// 写入java HashMap对象，会自动转换成Flash的Object对象
		amfOut.writeObject(map);
		dataOutStream.flush();

		byte[] ba = outStream.toByteArray();
		
		dataOutStream.close();
		outStream.close();
		amfOut.close();
            System.out.println(bytesToHex(ba));
		return ba;
	}

	public static ASObject toASObject(byte[] ba) throws ClassNotFoundException, IOException {
		ByteArrayInputStream bi = new ByteArrayInputStream(ba);
		SerializationContext serializationContext = new SerializationContext();
		Amf3Input amfin = new Amf3Input(serializationContext);
		
		amfin.setInputStream(bi);
		Object obj = amfin.readObject();
		amfin.close();
		bi.close();
		
		return (ASObject) obj;
	}

    public static String bytesToHex(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv).append('-');
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException {
		ASObject obj =  toASObject(toByteArray("abc", new Object()));
		System.out.println(obj);
	}
}

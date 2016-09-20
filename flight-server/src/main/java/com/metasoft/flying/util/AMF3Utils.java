package com.metasoft.flying.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;
import flex.messaging.io.amf.Amf3Output;

public class AMF3Utils {
	private static final Logger logger = LoggerFactory.getLogger(AMF3Utils.class);
	
	public static final SerializationContext serializationContext = new SerializationContext();

	private static int CACHE_SIZE = 1024;
	private static final Map<String, int[]> statMap = new ConcurrentHashMap<String, int[]>();
	private static long lastStatMs = System.currentTimeMillis();

	static {
		serializationContext.legacyCollection = true;
	}

	public static byte[] getAmf3ByteArray(Object obj) {
		if ((obj instanceof byte[])) {
			return (byte[]) obj;
		}
		Amf3Output amf3out = null;
		ByteArrayOutputStream bos = null;
		try {
			amf3out = new Amf3Output(serializationContext);
			bos = new ByteArrayOutputStream();
			amf3out.setOutputStream(bos);
			amf3out.writeObject(obj);
			amf3out.flush();
			byte[] srcBs = bos.toByteArray();
			byte[] bs = compressBytes(srcBs);

			if (logger.isDebugEnabled()) {
				stat(obj.getClass().getName(), bs.length);
			}
			return bs;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				amf3out.close();
				bos.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ArrayUtils.EMPTY_BYTE_ARRAY;
	}

	public static byte[] getAmf3ByteArrayWithoutCompress(Object obj) {
		if ((obj instanceof byte[])) {
			return (byte[]) obj;
		}
		Amf3Output amf3out = null;
		ByteArrayOutputStream bos = null;
		try {
			amf3out = new Amf3Output(serializationContext);
			bos = new ByteArrayOutputStream();
			amf3out.setOutputStream(bos);
			amf3out.writeObject(obj);
			amf3out.flush();
			byte[] bs = bos.toByteArray();

			if (logger.isDebugEnabled()) {
				stat(obj.getClass().getName(), bs.length);
			}
			return bs;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				amf3out.close();
				bos.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return ArrayUtils.EMPTY_BYTE_ARRAY;
	}

	private static void stat(String name, int len) {
		int[] stats = statMap.get(name);
		if (stats == null) {
			stats = new int[2];
			statMap.put(name, stats);
		}
		stats[0] += 1;
		stats[1] += len;
		if ((statMap.size() > 100)
				|| (System.currentTimeMillis() - lastStatMs > 60000L)) {
			ReentrantLock lock = new ReentrantLock();
			lock.lock();
			Map<String, int[]> cpMap = null;
			try {
				lastStatMs = System.currentTimeMillis();
				cpMap = new HashMap<String, int[]>(statMap);
				statMap.clear();
			} finally {
				lock.unlock();
			}
			if ((cpMap != null) && (cpMap.size() > 0))
				for (Map.Entry<String, int[]> entry : cpMap.entrySet()) {
					int[] ss = (int[]) entry.getValue();
					if (ss != null)
						logger.debug((String) entry.getKey() + "|" + ss[0]+ "|" + ss[1]);
				}
		}
	}

	public static Object getObjectFromAmf3ByteArray(byte[] bs) {
		Amf3Input amf3in = null;
		ByteArrayInputStream bis = null;
		try {
			amf3in = new Amf3Input(serializationContext);
			bis = new ByteArrayInputStream(decompressBytes(bs));
			amf3in.setInputStream(bis);
			return amf3in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				amf3in.close();
				bis.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public static Object getObjectFromAmf3ByteArrayWithoutDecompress(byte[] bs) {
		Amf3Input amf3in = null;
		ByteArrayInputStream bis = null;
		try {
			amf3in = new Amf3Input(serializationContext);
			bis = new ByteArrayInputStream(bs);
			amf3in.setInputStream(bis);
			return amf3in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				amf3in.close();
				bis.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public static byte[] decompressBytes(byte[] input) {
		Inflater decompresser = new Inflater();
		byte[] output = new byte[0];
		decompresser.reset();
		decompresser.setInput(input);
		ByteArrayOutputStream o = new ByteArrayOutputStream(input.length);
		try {
			byte[] buf = new byte[CACHE_SIZE];

			while (!decompresser.finished()) {
				int got = decompresser.inflate(buf);
				o.write(buf, 0, got);
			}
			output = o.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			decompresser.end();
			decompresser = null;
		}
		return output;
	}

	public static byte[] compressBytes(byte[] input) {
		Deflater compresser = new Deflater();
		compresser.reset();
		compresser.setInput(input);
		compresser.finish();
		byte[] output = new byte[0];
		ByteArrayOutputStream o = new ByteArrayOutputStream(input.length);
		try {
			byte[] buf = new byte[compressBound(input.length)];

			while (!compresser.finished()) {
				int got = compresser.deflate(buf);
				o.write(buf, 0, got);
			}
			output = o.toByteArray();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			compresser.end();
			compresser = null;
		}
		return output;
	}

	public static int compressBound(int sourceLen) {
		return sourceLen + (sourceLen >> 12) + (sourceLen >> 14)
				+ (sourceLen >> 25) + 13;
	}
}

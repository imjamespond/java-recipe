package com.chitu.poker.arena;

import java.nio.ByteBuffer;
import java.util.LinkedList;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.persist.GenericDao;
import cn.gecko.persist.PersistObject;
import cn.gecko.persist.annotation.PersistEntity;

/**
 * 竞技场持久
 * @author open
 *
 */
@Entity
@Table(name = "poker_arena")
@PersistEntity(cache = false)
public class PersistArena extends PersistObject {

	private long id;
	
	private byte[] logs;
	
	public static PersistArena get(long id) {
		return PersistObject.get(PersistArena.class, id);
	}
	
	public static int getMaxRank() {
		return SpringUtils.getBeanOfType(GenericDao.class).count(PersistArena.class,"");
	}
	
	public static LinkedList<ArenaLog> initLogs(byte[] byteData){
		LinkedList<ArenaLog> logs = new LinkedList<ArenaLog>();
		if (byteData == null || byteData.length == 0) {
			return logs;
		}
		
		ByteBuffer buffer = ByteBuffer.wrap(byteData);
		byte version = buffer.get();
		if (version == ArenaLog.LOG_VERSION_1) {
			int size = buffer.getInt();
			for(int i=0;i<size;i++){
				ArenaLog log = new ArenaLog();
				log.initBuffer(buffer);
				logs.add(log);
			}
		}
		return logs;
	}
	
	public static byte[] logsData(LinkedList<ArenaLog> logs) {
		int length = 1 + 4;
		for(ArenaLog log : logs){
			length += log.byteLength();
		}
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.put(ArenaLog.LOG_VERSION);
		buffer.putInt(logs.size());
		for(ArenaLog log : logs){
			log.toBuffer(buffer);
		}

		buffer.flip();
		byte[] data = new byte[buffer.limit()];
		buffer.get(data);
		return data;
	}
	
	@Override
	public Long id() {
		return id;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public byte[] getLogs() {
		return logs;
	}

	public void setLogs(byte[] logs) {
		this.logs = logs;
	}
	
}

package com.chitu.chess.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.gecko.commons.data.StaticConfig;
import cn.gecko.persist.PersistObject;
import cn.gecko.persist.annotation.PersistEntity;
import cn.gecko.player.model.Backpack;
import cn.gecko.player.model.BindType;
import cn.gecko.player.model.PackItem;

@Entity
@Table(name = "td_backpack")
@PersistEntity(cache = false)
public class PersistBackpack extends PersistObject {

	public static final byte BACKPACK_VERSION_1 = 1;

	public static final byte BACKPACK_VERSION = BACKPACK_VERSION_1;

	private long id;

	private byte[] backpack;

	public static PersistBackpack get(long id) {
		return get(PersistBackpack.class, id);
	}

	public Long id() {
		return id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte[] getBackpack() {
		return backpack;
	}

	public void setBackpack(byte[] backpack) {
		this.backpack = backpack;
	}

	public static Backpack initBackpack(int backpackId, long playerId, byte[] backpack) {
		if (backpack == null || backpack.length == 0)
			return null;
		ByteBuffer buffer = ByteBuffer.wrap(backpack);
		byte version = buffer.get();
		Backpack bp = null;
		if (version == BACKPACK_VERSION_1) {
			int cap = buffer.getInt();
			int size = buffer.getInt();
			List<PackItem> allItems = new ArrayList<PackItem>(size);
			bp = new ChessBackpack(backpackId, playerId, cap, allItems);
			for (int i = 0; i < size; i++) {
				int itemIndex = buffer.getInt();
				int itemId = buffer.getInt();
				int itemCount = buffer.getInt();
				int bindType = buffer.get();
				long uniqueId = buffer.getLong();
				long expiredTime = buffer.getLong();
				ChessPackItem packItem = new ChessPackItem();
				packItem.itemIndex=itemIndex;
				packItem.itemId=itemId;
				packItem.itemCount=itemCount;
				packItem.bindType=BindType.values()[bindType];
				packItem.uniqueId=uniqueId;
				packItem.expiredTime=expiredTime;
				packItem.backpack=bp;
				allItems.add(packItem);
			}
			bp.refresh();
		}
		return bp;
	}

	public static byte[] backpackData(Backpack backpack) {
		List<PackItem> allItems = backpack.allItems;
		int length = 1 + 4 + 4 + allItems.size() * (4 + 4 + 4 + 1 + 8 + 8);
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.put(BACKPACK_VERSION);
		buffer.putInt(backpack.capability);
		buffer.putInt(allItems.size());
		for (PackItem item : allItems) {
			ChessPackItem packItem = (ChessPackItem) item;
			buffer.putInt(packItem.itemIndex);
			buffer.putInt(packItem.itemId);
			buffer.putInt(packItem.itemCount);
			buffer.put((byte) packItem.bindType.ordinal());
			buffer.putLong(packItem.uniqueId);
			buffer.putLong(packItem.expiredTime);
		}
		buffer.flip();
		byte[] data = new byte[buffer.limit()];
		buffer.get(data);
		return data;
	}

	public static Backpack createBackpack(int backpackId, long playerId) {
		int initCap = StaticConfig.get(ChessStaticConfigs.BACKPACK_INIT_CAPABILITY).getAsInt(500);
		List<PackItem> items = new ArrayList<PackItem>(initCap);
		return new ChessBackpack(backpackId, playerId, initCap, items);
	}

	private static class ChessBackpack extends Backpack {
		public ChessBackpack(int id, long playerId, int capability, List<PackItem> allItems) {
			super(id, playerId, capability, allItems);
		}

		@Override
		public PackItem createPackItem() {
			ChessPackItem pi= new ChessPackItem();
			pi.backpack=this;
			return pi;
		}

	}

}

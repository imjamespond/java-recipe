package com.metasoft.empire.model;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("user_data")
public class UserDataPersist {
	//private static final Logger logger = LoggerFactory.getLogger(UserDataPersist.class);
	public UserDataPersist() {

	}

	public UserDataPersist(long id) {
		super();
		this.id = id;
	}
	@Id("data_id_seq")
	@Column("id")
	private Long id;
	@Column("guide")
	private long guide;
	@Column("score")
	private int score;//经验
	@Column("level")
	private int level;
	@Column("redeem")
	private int redeem;
	@Column("relationship")
	private byte[] relationship= new byte[0];// 8userId
	@Column("item")
	private byte[] item= new byte[0];// 4id-4num
	@Column("fashion")
	private byte[] fashion= new byte[0];// 4id-8到期时间
	@Column("gift")
	private byte[] gift= new byte[0];// 4id-8到期时间
	@Column("upgrade")
	private byte[] upgrade= new byte[0];// 4id-8到期时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getRelationship() {
		return relationship;
	}

	public void setRelationship(byte[] relationship) {
		this.relationship = relationship;
	}

	public byte[] getItem() {
		return item;
	}

	public void setItem(byte[] item) {
		this.item = item;
	}

	public byte[] getFashion() {
		return fashion;
	}

	public void setFashion(byte[] fashion) {
		this.fashion = fashion;
	}

	public byte[] getGift() {
		return gift;
	}

	public void setGift(byte[] gift) {
		this.gift = gift;
	}

	public byte[] getUpgrade() {
		return upgrade;
	}

	public void setUpgrade(byte[] upgrade) {
		this.upgrade = upgrade;
	}
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public long getGuide() {
		return guide;
	}

	public void setGuide(long guide) {
		this.guide = guide;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRedeem() {
		return redeem;
	}

	public void setRedeem(int redeem) {
		this.redeem = redeem;
	}

	public Map<Integer, UserItem> initItemMap() {
		Map<Integer, UserItem> map = new HashMap<Integer, UserItem>();
		ByteBuffer buffer = ByteBuffer.wrap(item);
		byte version = '\0';
		// read version
		if (buffer.remaining() > 0) {
			version = buffer.get();
		}
		// read data
		if (version == 'a') {
			while (buffer.remaining() >= 8) {
				int itemId = buffer.getInt();
				int itemNum = buffer.getInt();
				map.put(itemId, new UserItem(itemId, itemNum));
			}
		} else if (version == 'b') {
			while (buffer.remaining() >= 16) {
				int itemId = buffer.getInt();
				int itemNum = buffer.getInt();
				long itemTime = buffer.getLong();
				map.put(itemId, new UserItem(itemId, itemNum, itemTime));
			}
		}
		return map;
	}

	public void itemMapToBytes(Map<Integer, UserItem> map) {
		int length = 1 + map.size() * (4 + 4 + 8);// map长度*(itemId + itemNum +
													// 获取时间)
		ByteBuffer buffer = ByteBuffer.allocate(length);
		// version
		byte version = 'b';
		buffer.put(version);
		// put data
		if (version == 'a') {
			for (Map.Entry<Integer, UserItem> entry : map.entrySet()) {
				buffer.putInt(entry.getKey());
				buffer.putInt(entry.getValue().getItemNum());
			}
		} else if (version == 'b') {
			for (Map.Entry<Integer, UserItem> entry : map.entrySet()) {
				buffer.putInt(entry.getKey());
				buffer.putInt(entry.getValue().getItemNum());
				buffer.putLong(entry.getValue().getItemTime());
			}
		}
		item = buffer.array();
	}

	public Map<Integer, UserFashion> initFashionMap() {
		Map<Integer, UserFashion> map = new HashMap<Integer, UserFashion>();
		ByteBuffer buffer = ByteBuffer.wrap(fashion);
		byte version = '\0';
		// read version
		if (buffer.remaining() > 0) {
			version = buffer.get();
		}
		// read data
		if (version == 'a') {
			while (buffer.remaining() >= 16) {
				int pos = buffer.getInt();
				int itemId = buffer.getInt();
				long deadline = buffer.getLong();
				map.put(pos, new UserFashion(itemId, deadline));
			}
		}
		return map;
	}

	public void fashionMapToBytes(Map<Integer, UserFashion> map) {
		int length = 1 + map.size() * (4 + 4 + 8);// map长度*(pos + itemId + 时间)
		ByteBuffer buffer = ByteBuffer.allocate(length);
		// version
		byte version = 'a';
		buffer.put(version);
		// put data
		if (version == 'a') {
			for (Entry<Integer, UserFashion> entry : map.entrySet()) {
				buffer.putInt(entry.getKey());
				UserFashion fashion = entry.getValue();
				if (null == fashion) {
					buffer.putInt(0);
					buffer.putLong(0l);
				} else {
					buffer.putInt(fashion.getItemId());
					buffer.putLong(fashion.getDeadline());
				}
			}
		}
		fashion = buffer.array();
	}

	public Map<Long, UserFollow> initFollowingMap() {
		Map<Long, UserFollow> set = new HashMap<Long, UserFollow>();
		ByteBuffer buffer = ByteBuffer.wrap(relationship);
		byte version = 0;
		// read version
		if (buffer.remaining() > 0) {
			version = buffer.get();
		}
		// read data
		if (version == 'a') {
			while (buffer.remaining() >= 12) {
				int count = buffer.getInt();
				long userId = buffer.getLong();
				set.put(userId, new UserFollow(count, 0, userId));
			}
		} else if (version == 'b') {
			while (buffer.remaining() >= 16) {
				int count = buffer.getInt();
				int state = buffer.getInt();
				long userId = buffer.getLong();
				UserFollow uf = new UserFollow(count, state, userId);
				set.put(userId, uf);
			}
		}// else other version...
		return set;
	}

	public void followingMapToBytes(Map<Long, UserFollow> map) {
		int length = 1 + map.size() * (4 + 4 + 8);// set长度*(count + state +
													// userId)
		ByteBuffer buffer = ByteBuffer.allocate(length);
		// version
		byte version = 'b';
		buffer.put(version);
		// put data
		if (version == 'a') {
			for (Entry<Long, UserFollow> entry : map.entrySet()) {
				UserFollow uf = entry.getValue();
				buffer.putInt(uf.getCount());
				buffer.putLong(uf.getUserId());
			}
		} else if (version == 'b') {
			for (Entry<Long, UserFollow> entry : map.entrySet()) {
				UserFollow uf = entry.getValue();
				buffer.putInt(uf.getCount());
				buffer.putInt(uf.getState());
				buffer.putLong(uf.getUserId());
			}
		}
		relationship = buffer.array();
	}

	public Map<Integer, UserUpgrade> initUpgradeMap() {
		Map<Integer, UserUpgrade> set = new HashMap<Integer, UserUpgrade>();
		ByteBuffer buffer = ByteBuffer.wrap(upgrade);
		byte version = 0;
		// read version
		if (buffer.remaining() > 0) {
			version = buffer.get();
		}
		// read data
		if (version == 'b') {
			while (buffer.remaining() >= 16) {
				int role = buffer.getInt();
				int count = buffer.getInt();
				int upgrade = buffer.getInt();
				int level = buffer.getInt();
				
				UserUpgrade uu = new UserUpgrade(role, count, upgrade, level);
				set.put(role, uu);
			}
		}// else other version...
		return set;
	}

	public void upgradeMapToBytes(Map<Integer, UserUpgrade> map) {
		int length = 1 + map.size() * (4+4+4+4);// set长度*(count + state +
													// userId)
		ByteBuffer buffer = ByteBuffer.allocate(length);
		// version
		byte version = 'b';
		buffer.put(version);
		// put data
		if (version == 'b') {
			for (Entry<Integer, UserUpgrade> entry : map.entrySet()) {
				UserUpgrade uf = entry.getValue();
				buffer.putInt(uf.getRoleid());
				buffer.putInt(uf.getNumber());
				buffer.putInt(uf.getUpgrade());
				buffer.putInt(uf.getLevel());
			}
		}
		upgrade = buffer.array();
	}

}

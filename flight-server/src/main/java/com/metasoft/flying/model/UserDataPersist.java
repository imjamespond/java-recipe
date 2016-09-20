package com.metasoft.flying.model;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.util.EpochUtil;
import com.metasoft.flying.vo.ChessScoreVO;
import com.metasoft.flying.vo.UserGiftVO;

@Table("fly_data")
public class UserDataPersist {
	//private static final Logger logger = LoggerFactory.getLogger(UserDataPersist.class);
	public UserDataPersist() {

	}

	public UserDataPersist(long id) {
		super();
		this.id = id;
	}
	@Id("fly_data_seq")
	@Column("id")
	private Long id;
	@Column("guide")
	private long guide;
	@Column("pve")
	private int pve;//闯关数
	@Column("pvetime")
	private long pvetime;//闯关时间
	@Column("matchnum")
	private int matchnum;//比赛数
	@Column("matchepoch")
	private long matchepoch;//比赛日期
	@Column("score")
	private int score;//经验
	@Column("contribute")
	private int contribute;
	@Column("gold")
	private int gold;
	@Column("charm")
	private int charm;
	@Column("group_")
	private String group_ = "";
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
	@Column("matchrec")
	private byte[] matchrec= new byte[0];// 4id-8到期时间

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
	public String getGroup_() {
		return group_;
	}

	public void setGroup_(String group_) {
		this.group_ = group_;
	}
	public int getContribute() {
		return contribute;
	}

	public void setContribute(int contribute) {
		this.contribute = contribute;
	}

	public int getCharm() {
		return charm;
	}

	public void setCharm(int charm) {
		this.charm = charm;
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

	public int getPve() {
		return pve;
	}

	public void setPve(int pve) {
		this.pve = pve;
	}

	public long getPvetime() {
		return pvetime;
	}

	public void setPvetime(long pvetime) {
		this.pvetime = pvetime;
	}

	public int getMatchnum() {
		return matchnum;
	}

	public void setMatchnum(int matchnum) {
		this.matchnum = matchnum;
	}

	public long getMatchepoch() {
		return matchepoch;
	}

	public void setMatchepoch(long matchepoch) {
		this.matchepoch = matchepoch;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public byte[] getMatchrec() {
		return matchrec;
	}

	public void setMatchrec(byte[] matchrec) {
		this.matchrec = matchrec;
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

	public List<UserGiftVO> initGiftList() {
		List<UserGiftVO> list = new ArrayList<UserGiftVO>();
		ByteBuffer buffer = ByteBuffer.wrap(gift);
		byte version = '\0';
		// read version
		if (buffer.remaining() > 0) {
			version = buffer.get();
		}
		// read data
		if (version == 'a') {
			while (buffer.remaining() >= 24) {
				int itemId = buffer.getInt();
				int itemNum = buffer.getInt();
				long userId = buffer.getLong();
				long date = buffer.getLong();
				list.add(new UserGiftVO(itemId, itemNum, userId, date));
			}
		} else if (version == 'b') {
			while (buffer.remaining() >= 28) {
				int itemId = buffer.getInt();
				int itemNum = buffer.getInt();
				int thank = buffer.getInt();
				long userId = buffer.getLong();
				long date = buffer.getLong();
				UserGiftVO vo = new UserGiftVO(itemId, itemNum, userId, date);
				vo.setThank(thank);
				list.add(vo);
			}
		}
		return list;
	}

	public void giftListToBytes(List<UserGiftVO> list) {
		int length = 1 + list.size() * (4 + 4 + 8 + 8 + 4);// map长度*(itemId +
															// itemNum + userId
															// + 时间 + thank)
		ByteBuffer buffer = ByteBuffer.allocate(length);
		// version
		byte version = 'b';
		buffer.put(version);
		// put data
		if (version == 'a') {
			for (UserGiftVO gift : list) {
				buffer.putInt(gift.getItemId());
				buffer.putInt(gift.getItemNum());
				buffer.putLong(gift.getUserId());
				buffer.putLong(gift.getDate());
			}
		} else if (version == 'b') {
			for (UserGiftVO gift : list) {
				buffer.putInt(gift.getItemId());
				buffer.putInt(gift.getItemNum());
				buffer.putInt(gift.getThank());
				buffer.putLong(gift.getUserId());
				buffer.putLong(gift.getDate());
			}
		}
		gift = buffer.array();
	}

	public int[] initUpgrade(User user) {
		int[] list = new int[ChessPlayer.ITEM_LEN];
		ByteBuffer buffer = ByteBuffer.wrap(upgrade);
		byte version = '\0';
		// read version
		if (buffer.remaining() > 0) {
			version = buffer.get();
		}
		// read data
		if (version == 'a') {
		} else if (version == 'b' && buffer.remaining() >= 16) {
			user.pveEpoch = buffer.getLong();
			user.pveNum = buffer.getInt();
			/*user.pveLevel = */int level = buffer.getInt();this.pve=this.pve<level?level:this.pve;//FIXME 
			int i = 0;
			while (buffer.remaining() >= 4) {
				int num = buffer.getInt();
				if(i<list.length)
				list[i++] = num;
			}
			//logger.debug("user data id:{}, epoch:{}, num:{}, lv:{}",id, user.pveEpoch,user.pveNum,user.pveLevel);
		}
		return list;
	}
	public void upgradeToBytes(int[] ugrade, long epoch, int num) {
		int length = 1 + 8 + 4 + 4 + ugrade.length * (4);// epoch(8) + num(4) + level(4) + 长度*(num)
		ByteBuffer buffer = ByteBuffer.allocate(length);
		// version
		byte version = 'b';
		buffer.put(version);
		// put data
		if (version == 'a') {
		} else if (version == 'b') {
			buffer.putLong(epoch);
			buffer.putInt(num);
			buffer.putInt(0);//deprecated
			for (int ug : ugrade) {
			buffer.putInt(ug);
			}
		}
		upgrade = buffer.array();
	}
	
	private static final int gkMatchLen = 32+16+8+8+4;// [id(8)*4 + score(4)*4 + mtime(8) + duration(8) + win(4)] * size
	public Deque<UserMatch> initMatchRec() {		
		Deque<UserMatch> deq = new ArrayDeque<UserMatch>();
		ByteBuffer buffer = ByteBuffer.wrap(matchrec);
		byte version = '\0';
		// read version
		if (buffer.remaining() > 0) {
			version = buffer.get();
		}
		// read data
		if (version == 'a') {
		} else if (version == 'b') {
			while (buffer.remaining() >= gkMatchLen) {
				UserMatch um = new UserMatch();
				List<ChessScoreVO> scores = new ArrayList<ChessScoreVO>(4);
				for(int i=0;i<4;i++){
					ChessScoreVO vo = new ChessScoreVO();
					vo.setUserId(buffer.getLong());
					vo.setScore(buffer.getInt());
					if(vo.getUserId()>0)
						scores.add(vo);
				}
				um.setScores(scores);
				um.setMtime(buffer.getLong());
				um.setDuration(buffer.getLong());
				um.setWin(buffer.getInt());
				deq.add(um) ;
			}
		}
		return deq;
	}
	public void matchToBytes(Deque<UserMatch> matchDeq) {		
		int length = 1 + (gkMatchLen) * matchDeq.size() ;
		ByteBuffer buffer = ByteBuffer.allocate(length);
		// version
		byte version = 'b';
		buffer.put(version);
		// put data
		if (version == 'a') {
		} else if (version == 'b') {
			for (UserMatch um : matchDeq) {
				for(int i=0;i<4;i++){
					List<ChessScoreVO> scores = um.getScores();
					if(i<scores.size()){
						buffer.putLong(scores.get(i).getUserId());
						buffer.putInt(scores.get(i).getScore());
					}
					else{
						buffer.putLong(0);
						buffer.putInt(0);
					}
						
				}
				buffer.putLong(um.getMtime());
				buffer.putLong(um.getDuration());
				buffer.putInt(um.getWin());
			}
		}
		matchrec = buffer.array();
	}

	public int getMatchNumber() {
		if(EpochUtil.getEpochDay()!=EpochUtil.getEpochDay(matchepoch)){
			matchnum = 0;
		}
		return GeneralConstant.MATCH_LIMIT-matchnum;
	}

}

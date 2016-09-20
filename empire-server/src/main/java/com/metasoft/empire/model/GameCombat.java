package com.metasoft.empire.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metasoft.empire.common.GeneralResponse;
import com.metasoft.empire.model.data.RoleData;
import com.metasoft.empire.service.StaticDataService;
import com.metasoft.empire.service.common.SpringService;
import com.metasoft.empire.vo.GameAnimVO;
import com.metasoft.empire.vo.GameCombatVO;

public class GameCombat {
	private static final Logger log = LoggerFactory.getLogger(GameCombat.class);
	public static final int TYPE_ATTACK = 1;
	public static final int TYPE_DEFENSE = 2;
	public static final int TYPE_HEAL = 4;
	public static final int TYPE_SPELL = 8;
	
	public static String combat(GameRoom game){
		GamePlayer player0 = game.player0;
		GamePlayer player1 = game.player1;
		int turn = game.turn;
		int state = game.state;
		GameCombatVO vo = new GameCombatVO();
		vo.setOper0(player0.swap);
		vo.setOper1(player1.swap);
		vo.setSwap0(player0.swap());
		vo.setSwap1(player1.swap());
		player0.setSwap(0, 0);
		player1.setSwap(0, 0);
		
		StaticDataService sds = SpringService.getBean(StaticDataService.class);
		for(int i=0; i<3; i++){
			int[] roles0 = player0.vo.getRoles();
			int[] roles1 = player1.vo.getRoles();
			
			RoleData role0 = sds.roleMap.get(roles0[i]);
			RoleData role1 = sds.roleMap.get(roles1[i]);
			
			if(null!=role0&&null!=role1){
				vo.getAnim0().add(CombatAnim( role0, role1, player0, player1));
				vo.getAnim1().add(CombatAnim( role1, role0, player1, player0));
				
				if(player0.vo.getHp()==0||player1.vo.getHp()==0){
					break;
				}
			}
		}

		vo.setTurn(turn);
		vo.setState(state);
		
		return GeneralResponse.newJsonObj(vo);
	}
	
	public static GameAnimVO CombatAnim(RoleData role0, RoleData role1, 
			GamePlayer player0, GamePlayer player1){
		GameAnimVO ga = new GameAnimVO();
		ga.id = role0.getId();
		//attack
		if(role0.getType()==TYPE_ATTACK){
			if(role0.getId()==1){
				YanWangAnim vo = new YanWangAnim();
				vo.hurt = player0.vo.getAttack();
				player1.vo.decreaseHp(vo.hurt);
				vo.hp = player1.vo.getHp();
				ga.anim = vo;
			}else if(role0.getId()==5){
				AnubisAnim vo = new AnubisAnim();
				vo.hurt = player0.vo.getAttack() + player1.vo.getHp() / player1.maxhp * player0.vo.getAttack();
				player1.vo.decreaseHp(vo.hurt);
				vo.hp = player1.vo.getHp();
				ga.anim = vo;
			}else if(role0.getId()==9){
				LinLuAnim vo = new LinLuAnim();
				vo.hurt = (int) (player0.vo.getAttack() * (1+.4*player0.linlu));
				player1.vo.decreaseHp(vo.hurt);
				vo.hp = player1.vo.getHp();
				player0.linlu++;
				ga.anim = vo;
			}else if(role0.getId()==13){
				KnightAnim vo = new KnightAnim();
				vo.hurt = player0.vo.getAttack();
				if(role1.getType()==GameCombat.TYPE_ATTACK){
					vo.hurt += player0.vo.getAttack();
				}
				player1.vo.decreaseHp(vo.hurt);
				vo.hp = player1.vo.getHp();
				ga.anim = vo;
			}
		}
		//defense
		else if(role0.getType()==TYPE_DEFENSE){
			if(role0.getId()==2){
				WuChangAnim vo = new WuChangAnim();
				ga.anim = vo;
			}else if(role0.getId()==6){
				SphinxAnim vo = new SphinxAnim();
				vo.critical(player0, player1);
				if(role1.getType() == GameCombat.TYPE_ATTACK)
					vo.reflect = (int) (player1.vo.getAttack()*.5);
				vo.hp = player1.vo.getHp();
				ga.anim = vo;
			}else if(role0.getId()==10){
				YuSheAnim vo = new YuSheAnim();
				//FIXME ????????
				if(role1.getType()==GameCombat.TYPE_ATTACK){
					player0.yushe++;
					vo.hurt = (int) (player0.vo.getAttack() * (.05*player0.yushe));
					vo.heal = (int) (player0.maxhp * (.05*player0.yushe));
					player1.vo.decreaseHp(vo.hurt);
					player0.vo.increaseHp(vo.heal, player0.maxhp);
				}
				vo.hp0 = player0.vo.getHp();
				vo.hp1 = player1.vo.getHp();
				ga.anim = vo;
			}else if(role0.getId()==14){
				TitanAnim vo = new TitanAnim();
				if(role1.getType()==GameCombat.TYPE_ATTACK){
					vo.hurt += (int) (player1.vo.getAttack()*1.5);
				}
				player1.vo.decreaseHp(vo.hurt);
				vo.hp = player1.vo.getHp();
				ga.anim = vo;
			}			
		}
		//heal
		else if(role0.getType()==TYPE_HEAL){
			if(role0.getId()==3){
				MengPoAnim vo = new MengPoAnim();
				vo.heal = (int) (player0.mengpo * player0.maxhp * .06);
				player0.vo.increaseHp(vo.heal, player0.maxhp);
				vo.hp = player0.vo.getHp();
				if(vo.check(role1)){
					player0.mengpo++;
				}
				ga.anim = vo;
			}else if(role0.getId()==7){
				PharaohAnim vo = new PharaohAnim();
				vo.heal(player0, player1);
				player0.vo.increaseHp(vo.heal, player0.maxhp);
				vo.hp = player0.vo.getHp();
				ga.anim = vo;
			}else if(role0.getId()==11){
				JiShenAnim vo = new JiShenAnim();				
				if(role1.getType()!=GameCombat.TYPE_ATTACK){
					vo.heal = (int) (player0.maxhp * .1);
					player0.vo.increaseHp(vo.heal, player0.maxhp);
				}		
				vo.hp = player0.vo.getHp();
				ga.anim = vo;
			}else if(role0.getId()==15){
				AngelAnim vo = new AngelAnim();
				if(role1.getType()!=GameCombat.TYPE_ATTACK){
					vo.heal = (int) (player0.maxhp * .35);
					player0.vo.increaseHp(vo.heal, player0.maxhp);
				}		
				vo.hp = player0.vo.getHp();
				ga.anim = vo;
			}
		}
		//spell
		else if(role0.getType()==TYPE_SPELL){
			if(role0.getId() == 4){
				NiuTouAnim vo = new NiuTouAnim();
				ga.anim = vo;
			}else if(role0.getId()==8){
				CleopatraAnim vo = new CleopatraAnim();
				ga.anim = vo;
			}else if(role0.getId()==12){
				YunShenAnim vo = new YunShenAnim();		
				ga.anim = vo;
			}else if(role0.getId()==16){
				MedusaAnim vo = new MedusaAnim();
				player0.petrify = player0.turn+1;
				ga.anim = vo;
			}
		}

		return ga;
	}
	
	static{
		log.debug(GameCombat.class.toString());
	}
}

class YanWangAnim {
	public int hurt;
	public int hp;
	public int shorten=2;
}
class WuChangAnim {
	public int prolong=2;
}
class MengPoAnim {
	public int heal;
	public int hp;
	public int num;
	
	public boolean check(RoleData target){
		if(null==target){
			return false;
		}else if(null!=target && target.getType()==GameCombat.TYPE_ATTACK){
			return false;
		}
		return true;
	}
}
class NiuTouAnim {
	public int shorten=2;
}

class AnubisAnim {
	public int hp;
	public int hurt;
}
class SphinxAnim {
	public int hurt;
	public int reflect;
	public int hp;
	public void critical(GamePlayer player0, GamePlayer player1) {
		int types0=0,types1=0;
		StaticDataService sds = SpringService.getBean(StaticDataService.class);
		for(int i=0; i<3; i++){
			int[] roles0 = player0.vo.getRoles();
			int[] roles1 = player1.vo.getRoles();
			
			RoleData role0 = sds.roleMap.get(roles0[i]);
			RoleData role1 = sds.roleMap.get(roles1[i]);
			
			if(null!=role0&&null!=role1){
				types0|=role0.getType();
				types1|=role1.getType();
			}
		}
		if(types0==types1){
			hurt = player0.vo.getAttack();
		}
	}
}
class PharaohAnim{
	public int heal;
	public int hp;
	public void heal(GamePlayer player0, GamePlayer player1) {
		int types0=0,types1=0;
		StaticDataService sds = SpringService.getBean(StaticDataService.class);

		int[] roles0 = player0.vo.getRoles();
		int[] roles1 = player1.vo.getRoles();
		
		RoleData role0 = sds.roleMap.get(roles0[3]);
		RoleData role1 = sds.roleMap.get(roles1[3]);
		
		if(null!=role0&&null!=role1){
			types0|=role0.getType();
			types1|=role1.getType();
		}

		if(types0==types1){
			heal = (int) (player0.maxhp * .2);
		}
	}
}
class CleopatraAnim{
	public int inverse=1;
}

class LinLuAnim{
	public int hp;
	public int hurt;
	public int num;
}
class YuSheAnim{
	public int hp1;
	public int hp0;
	public int heal;
	public int hurt;
	public int prolong;
}
class JiShenAnim{
	public int hp;
	public int heal;
}
class YunShenAnim{
	public int invisible=1;
}

class KnightAnim{
	public int hp;
	public int hurt;
	public int impact;
}
class TitanAnim{
	public int hp;
	public int hurt;
	public int reflect;
}
class AngelAnim{
	public int hp;
	public int heal;
}
class MedusaAnim{
	public int petrify=1;
}
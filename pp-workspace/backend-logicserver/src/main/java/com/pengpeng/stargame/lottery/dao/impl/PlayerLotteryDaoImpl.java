package com.pengpeng.stargame.lottery.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.lottery.dao.IPlayerlotteryDao;
import com.pengpeng.stargame.model.lottery.PlayerLottery;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:11
 */
@Component
@DaoAnnotation(prefix = "player.lottery.")
public class PlayerLotteryDaoImpl  extends RedisDao<PlayerLottery>  implements IPlayerlotteryDao {
    public static int FREELOTTERYNUM = 3;

    @Override
    public Class<PlayerLottery> getClassType() {
        return PlayerLottery.class;
    }

    @Override
    public PlayerLottery getPlayerLottery(String pid) {
        PlayerLottery playerLottery=this.getBean(pid);
        if(null == playerLottery){
            playerLottery = new PlayerLottery();
            playerLottery.setPid(pid);
            playerLottery.setNum(FREELOTTERYNUM);
            playerLottery.setRefreshTime(new Date());
            saveBean(playerLottery);
        }

        //是否是当日
        Calendar ca = Calendar.getInstance();
        ca.setTime(playerLottery.getRefreshTime());
        if(ca.get(Calendar.DAY_OF_YEAR) != Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
            playerLottery.setNum(FREELOTTERYNUM);
            saveBean(playerLottery);
        }

        if(playerLottery.getrRefreshTime() == null){
           playerLottery.setrRefreshTime(new Date(0));
        }

        return playerLottery;
    }

    @Override
    public int getFreeNum() {
        return FREELOTTERYNUM;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

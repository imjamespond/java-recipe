package com.pengpeng.stargame.log;

import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: mql
 * Date: 13-10-14
 * Time: 下午1:58
 */
@Component
public class GameLoggerWrite {
    private static  final Logger logger = Logger.getLogger("rpc");

    @Autowired
    private  IPlayerDao playerDao;

    public  void write(GameLogger gameLogger){

        Player player=playerDao.getBean(gameLogger.getPid());

        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(GameLogger.B_SPLIT);
        stringBuffer.append(player.getUserId()+GameLogger.SPLIT);
        stringBuffer.append(gameLogger.getType()).append(GameLogger.SPLIT).append(gameLogger.getPid());
        stringBuffer.append(GameLogger.SPLIT).append(gameLogger.getDate().getTime());
        if(gameLogger.getValue()!=null&&!gameLogger.getValue().equals("")){

            stringBuffer.append(GameLogger.SPLIT).append(gameLogger.getValue());
        }

        logger.info(stringBuffer.toString());
    }




}

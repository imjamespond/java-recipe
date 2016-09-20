package com.chitu.poker;

import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.gecko.broadcast.Channel;
import cn.gecko.commons.data.BillType;
import cn.gecko.player.model.ChannelTalkRule.ChannelType;
import cn.gecko.player.msg.ChannelTalk;

import com.chitu.poker.model.ExpType;
import com.chitu.poker.model.PokerBillTypes;
import com.chitu.poker.model.PokerExpTypes;
import com.chitu.poker.model.PokerPlayer;

@Service
public class DevScene {

	@Value("${dev.scene}")
	private boolean dev;

	private static boolean devScene;

	@PostConstruct
	public void init() {
		System.out.println("########### devScene:" + dev);
		System.out.println("########### Charset:" + Charset.defaultCharset().name());
		devScene = dev;
	}

	public static void devBroadcast(Channel channel, String message) {
		if (!devScene)
			return;
		try {
			channel.broadcast(new ChannelTalk(message, ChannelType.SystemChannel));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void chatCmd(PokerPlayer player, String message) {
		if (!devScene)
			return;
		try {
			String[] command = message.split(" ");
			if ("/money".equalsIgnoreCase(command[0])) {
				int amount = Integer.parseInt(command[1]);
				player.wealthHolder.increaseMoney(amount, BillType.get(PokerBillTypes.NULL), message);
			}
			if ("/exp".equalsIgnoreCase(command[0])) {
				int amount = Integer.parseInt(command[1]);
				player.incExp(amount, ExpType.get(PokerExpTypes.NULL), message);
			}
			if ("/point".equalsIgnoreCase(command[0])) {
				int amount = Integer.parseInt(command[1]);
				player.wealthHolder.increasePoint(amount, BillType.get(PokerBillTypes.NULL), message);
			}
			if ("/strength".equalsIgnoreCase(command[0])) {
				int amount = Integer.parseInt(command[1]);
				player.increaseStrength(amount, BillType.get(PokerBillTypes.NULL), "");
			}
			if ("/pet".equalsIgnoreCase(command[0])) {
				int staticId = Integer.parseInt(command[1]);
				int count = Integer.parseInt(command[2]);
				for(int i=0;i<count;i++){
					player.petHolder.addPet(staticId);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

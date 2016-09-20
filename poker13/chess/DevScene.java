package com.chitu.chess;

import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.gecko.broadcast.Channel;
import cn.gecko.commons.data.BillType;
import cn.gecko.player.model.ChannelTalkRule.ChannelType;
import cn.gecko.player.msg.ChannelTalk;

import com.chitu.chess.model.ChessBillTypes;
import com.chitu.chess.model.ChessPlayer;

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

	public static void chatCmd(ChessPlayer player, String message) {
		if (!devScene)
			return;
		try {
			String[] command = message.split(" ");
			if ("/money".equalsIgnoreCase(command[0])) {
				int amount = Integer.parseInt(command[1]);
				player.wealthHolder.increaseMoney(amount, 0,0, BillType.get(ChessBillTypes.NULL), message);
				player.wealthHolder.update();
			}
			if ("/point".equalsIgnoreCase(command[0])) {
				int amount = Integer.parseInt(command[1]);
				player.wealthHolder.increaseMoney(0,amount,0, BillType.get(ChessBillTypes.NULL), message);
				player.wealthHolder.update();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2013 年 07 月 09 日 06:30
-- 服务器版本: 5.5.16
-- PHP 版本: 5.4.0beta2-dev

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `chitu_chess`
--

-- --------------------------------------------------------

--
-- 表的结构 `chess_pay_order`
--

CREATE TABLE IF NOT EXISTS `chess_pay_order` (
  `id` bigint(20) NOT NULL,
  `playerId` bigint(20) NOT NULL,
  `playerName` varchar(99) NOT NULL DEFAULT '',
  `payTime` bigint(20) NOT NULL,
  `payMoney` int(11) NOT NULL,
  `payType` int(11) NOT NULL,
  `payStatus` int(11) NOT NULL,
  `payMob` varchar(11) DEFAULT '' COMMENT '充值手机号,长度11',
  `xlh` varchar(50) DEFAULT '' COMMENT '神州行充值卡序列号,长度50',
  `mm` varchar(50) DEFAULT '' COMMENT '神州行充值卡刮开密码,长度50',
  `sid` varchar(5) DEFAULT '' COMMENT '平台服务商订单号码,长度5',
  `notiflyTime` bigint(20) NOT NULL COMMENT '平台回复通知时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `chess_player`
--

CREATE TABLE IF NOT EXISTS `chess_player` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `accountId` varchar(99) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',
  `nickname` varchar(99) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '呢称',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '',
  `salt` varchar(36) NOT NULL DEFAULT '',
  `loginIp` varchar(22) DEFAULT '',
  `registryIp` varchar(22) DEFAULT '',
  `npc` tinyint(4) NOT NULL DEFAULT '0',
  `grade` int(11) NOT NULL DEFAULT '0' COMMENT '级别',
  `gender` tinyint(4) NOT NULL DEFAULT '0' COMMENT '性别',
  `avatar` tinyint(4) NOT NULL DEFAULT '0',
  `createTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `loginTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '登陆时间',
  `onlineTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '在线时间',
  `loginCount` int(11) NOT NULL DEFAULT '0',
  `battleId` int(11) NOT NULL DEFAULT '0',
  `money` int(11) NOT NULL DEFAULT '0' COMMENT '金币',
  `point` int(11) NOT NULL DEFAULT '0' COMMENT '积分',
  `prestige` int(11) NOT NULL DEFAULT '0' COMMENT '威望',
  `rmb` int(11) NOT NULL,
  `flags` bigint(20) NOT NULL DEFAULT '0',
  `exp` int(11) NOT NULL DEFAULT '0' COMMENT '经验',
  `victoryAmount` int(11) NOT NULL DEFAULT '0' COMMENT '胜利总局数',
  `gameAmount` int(11) NOT NULL DEFAULT '0' COMMENT '总局数',
  `victoryAmountEachday` int(11) NOT NULL DEFAULT '0',
  `gameAmountEachday` int(11) NOT NULL DEFAULT '0',
  `continuousVictory` int(11) NOT NULL DEFAULT '0' COMMENT '连胜局数',
  `continuousVictoryMax` int(11) NOT NULL DEFAULT '0' COMMENT '最大胜利总局数',
  `mission` blob NOT NULL COMMENT '任务1',
  `achievement` blob NOT NULL COMMENT '成就',
  `replay` blob NOT NULL,
  `buffVip` bigint(20) NOT NULL COMMENT 'Vipbuff',
  `buffSafebox` bigint(20) NOT NULL COMMENT '可使用保险箱buff',
  `buffExpression` bigint(20) NOT NULL COMMENT '可使用表情buff',
  `buffVipExpression` bigint(20) NOT NULL COMMENT '可使用Vip表情buff',
  `buffLoginMoneyGiving` blob NOT NULL COMMENT '每日登陆送金币buff',
  `buffMultiplePoint` bigint(20) NOT NULL COMMENT '积分翻倍buff',
  `buffMultiplePointVal` int(11) NOT NULL COMMENT '积分翻倍倍数',
  `speaker` int(11) NOT NULL COMMENT '喇叭次数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `accountId` (`accountId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

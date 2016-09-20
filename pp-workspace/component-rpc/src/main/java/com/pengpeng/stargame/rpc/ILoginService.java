package com.pengpeng.stargame.rpc;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-8下午5:07
 */
public interface ILoginService {

    /**
     * 创建角色
     * @param nickName
     * @param sex
     * @param type
     * @param userId
     * @return
     */
    public String createPlayer(String nickName,int sex,int type,int userId);
    /**
     * 退出帐号
     * @param pid
     */
    public void logout(Session session,String pid);

	/**
	 * 取得玩家id
	 * @param id
	 * @return playerId
	 */
    String getUid(Integer id);
}

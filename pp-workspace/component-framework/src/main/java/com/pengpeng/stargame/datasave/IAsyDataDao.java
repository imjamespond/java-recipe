package com.pengpeng.stargame.datasave;

/**
 * User: mql
 * Date: 14-1-13
 * Time: 下午2:01
 */
public interface IAsyDataDao {
    public DataValue get(String id);
    public void update(DataValue dataValue);

    /**
     * 当key 有改变，放入到 Redis队列里面
     * @param key
     */
    public void put(String key);

    /**
     * Reids里面的换成数据 往 数据库里面保存
     * @param key
     */
    public void reidsToDb(String key);

    /**
     * 数据库里面的数据 往 Redis里面保存
     * @param key
     */
    public  void dbToRedis(String key);

    /**
     * 获取一个变化的Key
     * @return
     */
    public String  pop();

    public long size();
}

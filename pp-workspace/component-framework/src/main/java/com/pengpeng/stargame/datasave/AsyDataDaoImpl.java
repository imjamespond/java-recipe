package com.pengpeng.stargame.datasave;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pengpeng.stargame.dao.RedisDB;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: mql
 * Date: 14-1-13
 * Time: 下午2:10
 */
@Component()
public class AsyDataDaoImpl implements IAsyDataDao {

    public static final String DATA_CHANGE_KEY = "changeKey";
    @Autowired
    private RedisDB redisDB;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    public int table_num;

    public int getTable_num() {
        return table_num;
    }

    public void setTable_num(int table_num) {
        this.table_num = table_num;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getTableId(String key) {
        int index = Math.abs(key.hashCode() % table_num);
        return String.valueOf(index);
    }

    @Override
    public DataValue get(String id) {
        String tableId = getTableId(id);
        String sql = "select * from dataValue" + tableId + " where id = ?";
        try {
            DataValue dataValue = jdbcTemplate.queryForObject(sql, new Object[]{new String(id)}, new RowMapper<DataValue>() {
                @Override
                public DataValue mapRow(ResultSet rs, int rowNum) throws SQLException {
                    DataValue dataValue = new DataValue();
                    dataValue.setId(rs.getString("id"));
                    dataValue.setValue(rs.getString("value"));
                    dataValue.setType(rs.getString("type"));
                    return dataValue;
                }
            });
            return dataValue;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public void update(DataValue dataValue) {
//        this.getHibernateTemplate().saveOrUpdate(dataValue);
        String tableId = getTableId(dataValue.getId());
        String sql = "REPLACE INTO dataValue" + tableId + " (id,type,value) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{dataValue.getId(), dataValue.getType(), dataValue.getValue()});
    }

    @Override
    public void put(String key) {
        redisDB.getRedisTemplate(DATA_CHANGE_KEY).boundListOps(DATA_CHANGE_KEY).remove(0, key);
        redisDB.getRedisTemplate(DATA_CHANGE_KEY).boundListOps(DATA_CHANGE_KEY).leftPush(key);
    }

    @Override
    public String pop() {
        return redisDB.getRedisTemplate(DATA_CHANGE_KEY).boundListOps(DATA_CHANGE_KEY).rightPop();
    }

    @Override
    public long size() {
        return redisDB.getRedisTemplate(DATA_CHANGE_KEY).boundListOps(DATA_CHANGE_KEY).size();
    }
    @Override
    public void reidsToDb(String key) {
        RedisTemplate<String, String> redisTemplate = redisDB.getRedisTemplate(key);
        if (redisTemplate.hasKey(key)) {
            String keytype = redisTemplate.type(key).toString();
            DataValue dataValue = new DataValue();
            dataValue.setId(key);
            dataValue.setType(keytype);
            if (keytype.equals("STRING")) {
                String value = redisTemplate.boundValueOps(key).get();
                dataValue.setValue(redisTemplate.boundValueOps(key).get());
            }
            if (keytype.equals("ZSET")) {
                Long size = redisTemplate.boundZSetOps(key).size();
                Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.boundZSetOps(key).rangeWithScores(0, size);
                Map<Double, String> valueMap = new HashMap<Double, String>();
                for (ZSetOperations.TypedTuple<String> t : set) {
                    valueMap.put(t.getScore(), t.getValue());
                }
                dataValue.setValue(gson.toJson(valueMap));
            }
            if (keytype.equals("LIST")) {
                Long size = redisTemplate.boundListOps(key).size();
                List<String> values = redisTemplate.boundListOps(key).range(0, size);
                dataValue.setValue(gson.toJson(values));

            }
            if (keytype.equals("HASH")) {
                BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(key);
                Map<String, String> items = ops.entries();
                dataValue.setValue(gson.toJson(items));
            }
            update(dataValue);
        }
    }

    @Override
    public void dbToRedis(String key) {
        //暂时不使用
        if (0 == 0) {
            return;
        }
        RedisTemplate<String, String> redisTemplate = redisDB.getRedisTemplate(key);
        //只有缓存里面 没数据的 情况下 才执行
        if (!redisTemplate.hasKey(key)) {
            DataValue dataValue = get(key);
            if (dataValue == null) { //数据库里面也没有
                return;
            }
            if (dataValue != null) {
                if (dataValue.getType().equals("STRING")) {
                    redisTemplate.boundValueOps(key).set(dataValue.getValue());
                }
                if (dataValue.getType().equals("ZSET")) {
                    Map<String, String> valueMap = gson.fromJson(dataValue.getValue(), new TypeToken<Map<String, String>>() {
                    }.getType());
                    for (String valuekey : valueMap.keySet()) {
                        redisTemplate.boundZSetOps(key).add(valueMap.get(valuekey), Double.parseDouble(valuekey));
                    }
                }
                if (dataValue.getType().equals("LIST")) {
                    List<String> values = gson.fromJson(dataValue.getValue(), new TypeToken<List<String>>() {
                    }.getType());
                    for (String value : values) {
                        redisTemplate.boundListOps(key).rightPush(value);
                    }
                }
                if (dataValue.getType().equals("HASH")) {
                    Map<String, String> items = gson.fromJson(dataValue.getValue(), new TypeToken<Map<String, String>>() {
                    }.getType());
                    HashOperations<String, String, String> opsTarget = redisTemplate.opsForHash();
                    for (Map.Entry<String, String> entry : items.entrySet()) {
                        opsTarget.put(key, entry.getKey(), entry.getValue());
                    }
                }
            }
        }
    }


}

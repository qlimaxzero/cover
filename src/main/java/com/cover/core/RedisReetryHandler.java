package com.cover.core;

import com.cover.util.TimeUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by zhaoqing on 2017/11/7.
 */
public class RedisReetryHandler implements ReentryHandler {

    private JedisPool jedisPool;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public boolean isReentry(String key, int repeatSec) {
        boolean isReentry = false;
        try (Jedis jedis = jedisPool.getResource()) {
            int nowTime = TimeUtil.getSecondTimestamp();
            String eTimeStr = jedis.getSet(key, String.valueOf(nowTime + repeatSec));
            if (eTimeStr == null || nowTime > Integer.parseInt(eTimeStr)) {
                isReentry = true;
            }
            jedis.expire(key, repeatSec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isReentry;
    }

}

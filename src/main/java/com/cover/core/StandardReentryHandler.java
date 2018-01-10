package com.cover.core;


import com.cover.util.ExpireMap;

/**
 * Created by zhaoqing on 2017/11/6.
 */
public class StandardReentryHandler implements ReentryHandler {

    private final ExpireMap<String, Integer> map = new ExpireMap<>();

    @Override
    public boolean isReentry(String key, int repeatSec) {
        boolean isReentry = false;
        try {
            if (map.get(key) == null) {
                map.put(key, 0, repeatSec);
                isReentry = true;
            } else {
                map.expire(key, repeatSec);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isReentry;
    }
}

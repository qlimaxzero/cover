package com.cover.util;


import java.util.*;


/**
 * Created by zhaoqing on 2017/11/6.
 */
public class ExpireMap<K, V> extends HashMap<K, V> {

    private final HashMap<K, Long> expireMap = new HashMap<>();

    public ExpireMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ExpireMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ExpireMap() {
        super();
    }

    public ExpireMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    @Override
    public int size() {
        clearUp();
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        clearUp();
        return super.isEmpty();
    }

    @Override
    public V get(Object key) {
        if (!checkExpire(key)) {
            super.remove(key);
            return null;
        }
        return super.get(key);
    }

    public V put(K key, V value, int sec) {
        expireMap.put(key, System.currentTimeMillis() + sec * 1000l);
        return super.put(key, value);
    }

    public boolean expire(K key, int sec) {
        if (super.containsKey(key)) {
            expireMap.put(key, System.currentTimeMillis() + sec * 1000l);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!checkExpire(key)) {
            super.remove(key);
            return false;
        }
        return super.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        clearUp();
        return super.containsValue(value);
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        clearUp();
        return super.entrySet();
    }

    @Override
    public Set<K> keySet() {
        clearUp();
        return super.keySet();
    }

    @Override
    public Collection<V> values() {
        clearUp();
        return super.values();
    }

    @Override
    public V remove(Object key) {
        expireMap.remove(key);
        return super.remove(key);
    }

    @Override
    public void clear() {
        expireMap.clear();
        super.clear();
    }

    private synchronized void clearUp() {
        Iterator<Map.Entry<K, V>> iterator = super.entrySet().iterator();
        while (iterator.hasNext()) {
            K key = iterator.next().getKey();
            if (!checkExpire(key)) {
                iterator.remove();
                super.remove(key);
            }
        }
    }

    private boolean checkExpire(Object key){
        if (expireMap.containsKey(key)) {
            if (System.currentTimeMillis() < expireMap.get(key).longValue()) {
                return true;
            } else {
                expireMap.remove(key);
                return false;
            }
        }
        return true;
    }

}

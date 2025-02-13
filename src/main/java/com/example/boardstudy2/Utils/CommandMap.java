package com.example.boardstudy2.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

//@Alias("hashmap")
public class CommandMap {
    Map<String, Object> map = new HashMap<String, Object>();

//    public Object getMap(String key) {return map.get(key);}

    /**
     * 데이터 가져오기
     * @param key
     * @return
     */
    public Object get(String key){
        return map.get(key);
    }

    /**
     * 데이터 담기
     * @param key
     * @param value
     * @return
     */
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    /**
     * 데이터 삭제
     * @param key
     * @return
     */
    public Object remove(String key) {
        return map.remove(key);
    }

    /**
     * 데이터 키, 값 삭제
     * @param key
     * @param value
     * @return
     */
    public boolean remove(Object key, Object value) {
        return map.remove(key, value);
    }

    /**
     * 데이터 초기화
     */
    public void clear() {
        map.clear();
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    public void putAll(Map<? extends String, ? extends Object> m) {
        map.putAll(m);
    }

    public Map<String, Object> getMap() {
        return map;
    }
}

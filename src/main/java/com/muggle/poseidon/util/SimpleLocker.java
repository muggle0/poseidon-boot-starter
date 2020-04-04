package com.muggle.poseidon.util;

import com.muggle.poseidon.base.PoseidonLocker;
import com.muggle.poseidon.base.exception.BasePoseidonCheckException;
import com.muggle.poseidon.base.exception.SimplePoseidonCheckException;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class SimpleLocker implements PoseidonLocker {

    private ConcurrentHashMap<String,CacheBean> lockMap;

    public SimpleLocker(){
        this.lockMap=new ConcurrentHashMap<>();
    }

    @Override
    public  synchronized boolean trylock(String key, Long express) {
        this.cleanExpiredKey();
        CacheBean locker = lockMap.get(key);
        if (locker!=null){
            Long timetep = locker.getTimetep();
            int i = timetep.compareTo(System.currentTimeMillis());
            // 未过期
            if (i>0){
                return false;
            }
        }
        CacheBean cacheBean = new CacheBean();
        cacheBean.setTimetep(System.currentTimeMillis()+express);
        this.lockMap.put(key,cacheBean);
        return true;
    }

    @Override
    public synchronized void dolock(String key,String value, Long express) throws BasePoseidonCheckException {
        Long time=System.currentTimeMillis()+30000L;
        CacheBean cacheBean = this.lockMap.get(key);
        while (time>System.currentTimeMillis()){
            if (cacheBean==null){
                CacheBean lockeBean = new CacheBean();
                lockeBean.setTimetep(System.currentTimeMillis()+express);
                lockeBean.setVaule(value);
                this.lockMap.put(key,lockeBean);
                return;
            }
            if (cacheBean.getVaule().equals(value)) {
                int i = cacheBean.getTimetep().compareTo(System.currentTimeMillis());
                if (i<0){
                    cacheBean.setVaule(value);
                    cacheBean.setTimetep(System.currentTimeMillis()+express);
                    return;
                }
            }
        }
        throw new SimplePoseidonCheckException("上锁失败：超时");
    }

    @Override
    public synchronized boolean unlock(String key,String value) {
        cleanExpiredKey();
        this.clean(key);
        return true;
    }

    @Override
    public void clean(String key) {
        ConcurrentHashMap<String, CacheBean> lockMap = this.lockMap;
        Set<Map.Entry<String, CacheBean>> entries = lockMap.entrySet();
        Iterator<Map.Entry<String, CacheBean>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, CacheBean> next = iterator.next();
            String mapKey = next.getKey();
            if (mapKey.equals(key)){
                iterator.remove();
                break;
            }
        }
    }

    private void cleanExpiredKey(){
        Set<Map.Entry<String, CacheBean>> entries = this.lockMap.entrySet();
        Iterator<Map.Entry<String, CacheBean>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, CacheBean> next = iterator.next();
            CacheBean value = next.getValue();
            if (value.getTimetep().compareTo(System.currentTimeMillis())<0){
                iterator.remove();
            }
        }
    }

    private class CacheBean{

        private Long timetep;

        private String vaule;

        public String getVaule() {
            return vaule;
        }

        public void setVaule(String vaule) {
            this.vaule = vaule;
        }

        public Long getTimetep() {
            return timetep;
        }

        public void setTimetep(Long timetep) {
            this.timetep = timetep;
        }

    }
}

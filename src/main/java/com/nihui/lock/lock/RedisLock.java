package com.nihui.lock.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Classname RedisLock
 * @Description TODO
 * @Date 2020/6/23 4:57 PM
 * @Created by nihui
 */
@Component
public class RedisLock implements Lock {
    private static final String lock = "lock";
    private ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean getLock() {
        boolean lockTemp = false;
        //如果当前没有获取过锁则进入新获取锁的逻辑
        if (stringThreadLocal.get()==null){
            Thread thread = new Thread(){
                @Override
                public void run() {
                    while (true){
                        //这里的操作就是不断的执行当前锁每隔10秒设置一个过期时间，这个过期时间就是30秒
                        stringRedisTemplate.expire(lock,30,TimeUnit.SECONDS);
                        try {
                            TimeUnit.SECONDS.sleep(100000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            String uuid= thread.getId()+":"+UUID.randomUUID().toString();
            stringThreadLocal.set(uuid);
            lockTemp = stringRedisTemplate.opsForValue().setIfAbsent(lock, uuid,30,TimeUnit.SECONDS);
            if (!lockTemp){
                while (true){
                    lockTemp = stringRedisTemplate.opsForValue().setIfAbsent(lock, uuid,30,TimeUnit.SECONDS);
                    if (lockTemp){
                        break;
                    }
                }
            }

            thread.start();
        }else if (stringThreadLocal.get().equals(stringRedisTemplate.opsForValue().get(lock))){
            return true;
        }
        return lockTemp;
    }
    @Override
    public boolean releaseLock() {

        String uuid = stringThreadLocal.get();
        Integer id = Integer.parseInt(uuid.split(":")[0]);
        Thread thread = findThread(id);
        thread.stop();
        if (uuid.equals(stringRedisTemplate.opsForValue().get(lock))){
            stringRedisTemplate.delete(lock);
            stringThreadLocal.remove();
            return true;
        }
        return false;
    }

    public static Thread findThread(long threadId){
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        while (group !=null){
            Thread[] threads = new Thread[(int)(group.activeCount()*1.2)];
            int count = group.enumerate(threads,true);
            for (int i = 0; i < count; i++) {
                if (threadId == threads[i].getId()){
                    return threads[i];
                }
            }
            group = group.getParent();
        }
        return null;
    }
}




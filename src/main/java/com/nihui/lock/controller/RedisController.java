package com.nihui.lock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname RedisController
 * @Description TODO
 * @Date 2020/6/23 11:44 AM
 * @Created by nihui
 */
@RestController
public class RedisController {

    /**
     * 1、首先在进入业务逻辑之前，先要获取到锁
     * <p>
     * 2、在完成业务逻辑之后要对锁进行释放
     */
    private final static String product = "nihui";

    private final static String lock = "lock";

    @Autowired
    private RedisTemplate<String, Object> stringRedisTemplate;

    @GetMapping("/get")
    public void get() {

        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(RedisController.lock, "lock");
        if (!lock){
            return;
        }
        String index = (String) stringRedisTemplate.opsForValue().get(product);
        Integer count = Integer.valueOf(index);
        System.out.println("现在还有库存" + count);
        if (count > 0) {
            stringRedisTemplate.opsForValue().set(product, String.valueOf(count - 1));
        } else {
            System.out.println("库存不足");
        }
        stringRedisTemplate.delete(RedisController.lock);
    }
//
//    @GetMapping("/get")
//    public void get() {
//
//        synchronized (this){
//            String index = (String) stringRedisTemplate.opsForValue().get(product);
//            Integer count = Integer.valueOf(index);
//            if (count>0){
//                stringRedisTemplate.opsForValue().set(product,String.valueOf(count-1));
//            }else {
//                System.out.println("库存不足");
//            }
//            System.out.println("现在还有库存" + count);
//        }
//
//    }
}


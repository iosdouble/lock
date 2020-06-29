package com.nihui.lock.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

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
    private Redisson redisson;
    @Autowired
    private RedisTemplate<String, Object> stringRedisTemplate;


    @GetMapping("/get")
    public void get() {
        // 超卖 问题
        // 获取锁
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(lock, lock,30,TimeUnit.SECONDS);
        if (!aBoolean){
            return ;
        }


        RLock lock = redisson.getLock(product);
        lock.lock();
        lock.tryLock();

        try {
            // 这里实现了一个简单逻辑
            int stock = Integer.parseInt((String) stringRedisTemplate.opsForValue().get(product));
            if (stock > 0) {
                // 这里就开始执行下单的逻辑
                stock = stock - 1;
                stringRedisTemplate.opsForValue().set(product, String.valueOf(stock));
                System.out.println("进行库存的减扣 ， 现在库存 " + stock);
            } else {
                System.out.println("库存减扣失败，库存不足");
            }
        }finally {
            //释放锁
            stringRedisTemplate.delete(RedisController.lock);
        }
    }


//    @GetMapping("/get")
//    public void get() {
//        // 超卖 问题
//        // 获取锁
//        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(lock, lock);
//        stringRedisTemplate.expire(lock,30,TimeUnit.SECONDS);
//        if (!aBoolean){
//            return ;
//        }
//        try {
//            // 这里实现了一个简单逻辑
//            int stock = Integer.parseInt((String) stringRedisTemplate.opsForValue().get(product));
//            if (stock > 0) {
//                // 这里就开始执行下单的逻辑
//                stock = stock - 1;
//                stringRedisTemplate.opsForValue().set(product, String.valueOf(stock));
//                System.out.println("进行库存的减扣 ， 现在库存 " + stock);
//            } else {
//                System.out.println("库存减扣失败，库存不足");
//            }
//        }finally {
//            //释放锁
//            stringRedisTemplate.delete(lock);
//        }
//    }

//    @GetMapping("/get")
//    public void get() {
//        // 超卖 问题
//        // 获取锁
//        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(lock, lock);
//        if (!aBoolean){
//            return ;
//        }
//        try {
//            // 这里实现了一个简单逻辑
//            int stock = Integer.parseInt((String) stringRedisTemplate.opsForValue().get(product));
//            if (stock > 0) {
//                // 这里就开始执行下单的逻辑
//                stock = stock - 1;
//                stringRedisTemplate.opsForValue().set(product, String.valueOf(stock));
//                System.out.println("进行库存的减扣 ， 现在库存 " + stock);
//            } else {
//                System.out.println("库存减扣失败，库存不足");
//            }
//        }finally {
//            //释放锁
//            stringRedisTemplate.delete(lock);
//        }
//    }


//    @GetMapping("/get")
//    public void get() {
//        // 超卖 问题
//        // 获取锁
//        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(lock, lock);
//        if (!aBoolean){
//            return ;
//        }
//        // 这里实现了一个简单逻辑
//        int stock = Integer.parseInt((String) stringRedisTemplate.opsForValue().get(product));
//        if (stock > 0) {
//            // 这里就开始执行下单的逻辑
//            stock = stock - 1;
//            stringRedisTemplate.opsForValue().set(product, String.valueOf(stock));
//            System.out.println("进行库存的减扣 ， 现在库存 " + stock);
//        } else {
//            System.out.println("库存减扣失败，库存不足");
//        }
//        //释放锁
//        stringRedisTemplate.delete(lock);
//
//    }


//    @GetMapping("/get")
//    public void get() {
//        // 超卖 问题
//        synchronized (product){
//            // 这里实现了一个简单逻辑
//            int stock = Integer.parseInt((String) stringRedisTemplate.opsForValue().get(product));
//            if (stock>0){
//                // 这里就开始执行下单的逻辑
//                stock = stock - 1;
//                stringRedisTemplate.opsForValue().set(product,String.valueOf(stock));
//                System.out.println("进行库存的减扣 ， 现在库存 "+stock);
//            }else {
//                System.out.println("库存减扣失败，库存不足");
//            }
//        }
//
//
//    }

//    @GetMapping("/get")
//    public void get() {
//
//        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(RedisController.lock, "lock");
//        if (!lock){
//            return;
//        }
//        String index = (String) stringRedisTemplate.opsForValue().get(product);
//        Integer count = Integer.valueOf(index);
//        System.out.println("现在还有库存" + count);
//        if (count > 0) {
//            stringRedisTemplate.opsForValue().set(product, String.valueOf(count - 1));
//        } else {
//            System.out.println("库存不足");
//        }
//        stringRedisTemplate.delete(RedisController.lock);
//    }
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


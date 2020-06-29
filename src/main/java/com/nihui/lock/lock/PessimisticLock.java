package com.nihui.lock.lock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Classname PessimisticLock
 * @Description TODO
 * @Date 2020/6/29 11:06 AM
 * @Created by nihui
 */
public class PessimisticLock {


    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.getAndIncrement();
    }

}

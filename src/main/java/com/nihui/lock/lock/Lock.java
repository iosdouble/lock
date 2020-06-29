package com.nihui.lock.lock;

/**
 * @Classname Lock
 * @Description TODO
 * @Date 2020/6/25 3:31 PM
 * @Created by nihui
 */
public interface Lock {
    public boolean getLock();
    public boolean releaseLock();
}

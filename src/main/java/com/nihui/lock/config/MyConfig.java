package com.nihui.lock.config;

import com.nihui.lock.filter.MyFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname MyConfig
 * @Description TODO
 * @Date 2020/6/23 11:54 AM
 * @Created by nihui
 */
@Configuration
public class MyConfig {

    public MyFilter myFilter(){
        return new MyFilter();
    }
}

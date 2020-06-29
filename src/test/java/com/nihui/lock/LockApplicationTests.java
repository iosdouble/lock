package com.nihui.lock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LockApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void tryTest(){
        System.out.println(test());
    }

    public String test(){
        try {
            int  a = 123;
            int  b = 0;

            System.out.println("执行代码");
            System.exit(-1);
            return "hello";
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("一定会执行么");
        }
        return ":";
    }

}

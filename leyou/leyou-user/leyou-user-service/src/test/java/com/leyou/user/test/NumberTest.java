package com.leyou.user.test;


import org.junit.Test;

import java.util.Random;

public class NumberTest {
    @Test
    public void test(){
        int min = Double.valueOf(Math.pow(10, 6 - 1)).intValue();

        int num = new Random().nextInt(Double.valueOf(Math.pow(10, 6 + 1)).intValue() - 1) + min;

        System.out.println(min + "---" + num);
    }
}

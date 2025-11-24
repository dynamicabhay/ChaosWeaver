package com.chaos.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DemoService {

    public int getRandom(){
        return new Random().nextInt();
    }

    public double getRandomDouble(){
        return Math.random()*25;
    }

    public void chaosMethod1(){
        System.out.println("hey its chaosMethod1");
    }

    public void chaosMethod2(){
        System.out.println("hey from chaosMethod2 ");
    }

    public void processOrder(){
        System.out.println(" hey from processOrder!");
    }
}

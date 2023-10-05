package com.cyl.arthas.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/thread")
public class ThreadController {
    @RequestMapping("running")
    public Object alwaysRun() {
        new Thread( () -> {
            while (true) {
                String str = UUID.randomUUID().toString().replaceAll("-", "");
            }
        },"cpu demo thread").start();

        new Thread( () -> {
            while (true) {
                String str = UUID.randomUUID().toString().replaceAll("-", "");
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"cpu with sleep thread").start();
        return "run";
    }
    private Object obj1 = new Object();
    private Object obj2 = new Object();

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        new Thread(() -> {
            synchronized (obj1){
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    synchronized (obj2){
                        System.out.printf("thread 1执行到此");
                    }
                }
            }
        },"thread 1").start();

        new Thread(() -> {
            synchronized (obj2) {
                synchronized (obj1){
                    System.out.printf("thread 2执行到此");
                }
            }
        },"thread 2").start();
        return "thread test";
    }


}
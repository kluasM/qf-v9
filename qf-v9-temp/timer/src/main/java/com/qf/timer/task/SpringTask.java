package com.qf.timer.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/19
 */
@Component
public class SpringTask {

    @Scheduled(cron = "0/2 * * * * ? ")
    public void run(){
        System.out.println(Thread.currentThread().getName());
        System.out.println(new Date());
    }
}

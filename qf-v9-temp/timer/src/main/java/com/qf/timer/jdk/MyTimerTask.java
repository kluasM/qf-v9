package com.qf.timer.jdk;

import java.util.Date;
import java.util.TimerTask;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/19
 */
public class MyTimerTask extends TimerTask {

    @Override
    public void run() {
        //
        System.out.println(Thread.currentThread().getName());
        System.out.println("now:"+new Date());
    }
}

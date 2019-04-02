package com.qf.timer.jdk;

import java.util.Date;
import java.util.Timer;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/19
 */
public class Main {
    public static void main(String[] args){
        Timer timer = new Timer();
        MyTimerTask task = new MyTimerTask();
        //
        System.out.println(new Date());
        //指定3秒后执行
        //timer.schedule(task,3000);
        //周期性执行
        timer.schedule(task,3000,1000);

        //局限性：
        //1.单线程
        //2.每月15号，我会定期在群里发红包10000，时间间隔不同
    }
}

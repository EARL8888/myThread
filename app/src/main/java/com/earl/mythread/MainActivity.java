package com.earl.mythread;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Runnable runnable;
    private Handler handler;
    private Button stopThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        //        enOpenInit();//不共享数据
        openInit();//共享数据
        //        myHandle();
        //        setlistenter();
    }

    private void setlistenter() {
        stopThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
            }
        });
    }

    private void initView() {
        stopThread = (Button) findViewById(R.id.stopThread);
    }

    /**
     * 共享数据
     */
    private void openInit() {
        MyThreadTwo myThreadTwo = new MyThreadTwo();
        Thread a = new Thread(myThreadTwo, "a");
        Thread b = new Thread(myThreadTwo, "b");
        Thread c = new Thread(myThreadTwo, "c");
        Thread d = new Thread(myThreadTwo, "d");
        Thread e = new Thread(myThreadTwo, "e");
        //主线程会一直启用结果一直为true
        System.out.println("Thread.currentThread().isAlive():+++++++++++++++" + "线程name： " + Thread.currentThread().getName() + "的结果：" + Thread.currentThread().isAlive());
        //自定义线程开启前为false
        System.out.println("myThreadTwo.isAlive():+++++++++++++++自定义线程开启前+" + "线程name： " + e.getName() + "的结果：" + e.isAlive());
        a.start();
        b.start();
        c.start();
        d.start();
        e.start();
        //主线程会一直启用结果一直为true
        System.out.println("Thread.currentThread().isAlive():+++++++++++++++" + "线程name： " + Thread.currentThread().getName() + "的结果：" + Thread.currentThread().isAlive());
        //自定义线程开启后为true
        System.out.println("myThreadTwo.isAlive():+++++++++++++++自定义线程开启后+" + "线程name： " + e.getName() + "的结果：" + e.isAlive());
        //自定义线程停止前
        System.out.println("自定义线程：" + e.isInterrupted());//isInterrupted()测试线程Thread对象是否已经是中断状态，但不清除状态标志
        //主线程停止前
        System.out.println("main线程停止：" + Thread.interrupted());//interrupted()测试线程Thread对象是否已经是中断状态，清除状态标志

        //自定义线程停止
        e.interrupt();
        System.out.println("自定义线程停止：" + e.isInterrupted());//isInterrupted()测试线程Thread对象是否已经是中断状态，但不清除状态标志

        //主线程停止
        Thread.currentThread().interrupt();
        System.out.println("main线程停止：" + Thread.interrupted());//interrupted()测试线程Thread对象是否已经是中断状态，清除状态标志

    }

    private void enOpenInit() {
        MyThreadOne a = new MyThreadOne("a");
        MyThreadOne b = new MyThreadOne("b");
        MyThreadOne c = new MyThreadOne("c");
        a.start();
        b.start();
        c.start();
    }


    //不共享数据
    public class MyThreadOne extends Thread {
        private int count = 5;

        public MyThreadOne(String name) {
            this.setName(name);//设置线程名称
        }

        @Override
        public void run() {
            super.run();
            while (count > 0) {
                count--;
                System.out.println("由" + this.currentThread().getName() + "计算 count=" + count);
            }
        }
    }

    //共享数据
    public class MyThreadTwo extends Thread {
        private int count = 5;

        @Override
        synchronized public void run() {
            super.run();
            count--;
            System.out.println("由" + this.currentThread().getName() + "计算 count=" + count);
        }
    }

    public void myHandle() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("被调用了");
                handler.postDelayed(runnable, 2000);
            }
        };
        new Thread(runnable).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}

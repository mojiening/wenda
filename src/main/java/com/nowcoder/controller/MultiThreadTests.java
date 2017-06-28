package com.nowcoder.controller;

import javax.swing.table.TableRowSorter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Administrator on 2017/5/13.
 */
class Consumer implements Runnable{
    private BlockingQueue<String> q;
    public Consumer(BlockingQueue<String> q)
    {
        this.q=q;
    }
    @Override
    public void run() {
        try
        {
            while (true)
            {
                System.out.println(Thread.currentThread().getName()+":"+q.take());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class Producer implements Runnable{
    private BlockingQueue<String> q;
    public Producer(BlockingQueue<String> q)
    {
        this.q=q;
    }
    @Override
    public void run() {
        try
        {
            for(int i=0;i<100;i++){
                Thread.sleep(100);
                q.put(String.valueOf(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
public class MultiThreadTests {

    public  static void testBlockingQueue(){
        BlockingQueue<String> q=new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q),"Consumer1").start();
        new Thread(new Consumer(q),"Consumer2").start();
    }

    private static ThreadLocal<Integer> threadLocal=new ThreadLocal<Integer>();
    private static int userId;
    public  static void testThreadLocal(){
        for(int i=0;i<10;i++){
            final int finalI=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        //threadLocal.set(finalI);
                        userId=finalI;
                        Thread.sleep(1000);
                        System.out.println("userId:"+userId);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }
     public static void main(String[] args){
      //  testBlockingQueue();
        testThreadLocal();
     }
}

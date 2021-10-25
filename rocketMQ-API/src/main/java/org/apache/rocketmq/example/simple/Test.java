package org.apache.rocketmq.example.simple;

import java.util.concurrent.CountDownLatch;

/**
 * @author 张群
 * @version 1.0
 * @date 2021/10/15 3:18 下午
 */
public class Test {

    private static volatile int a;
    private static volatile int b;
    private static volatile int x;
    private static volatile int y;

    public static void main(String[] args) throws InterruptedException {
        for (int c = 0; c<100000000; c++){
            CountDownLatch countDownLatch = new CountDownLatch(2);
            a =0;b=0;x=0;y=0;
            final Thread thread = new Thread(() -> {
                a = 1;
                x = b;
                countDownLatch.countDown();
            });
            final Thread thread1 = new Thread(() -> {
                b = 1;
                y = a;
                countDownLatch.countDown();
            });
            thread.start();
            thread1.start();
            countDownLatch.await();
     /*       System.out.println("-------------------");
            System.out.println("x = " + x);
            System.out.println("y = " + y);
            System.out.println("a = " + a);
            System.out.println("b = " + b);*/
            if (x==0&&y==0){
                System.out.println(x);
                System.out.println(y);
                break;
            }
        }


    }
}
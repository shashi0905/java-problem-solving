package com.shashi.practise.threads;

/**
 * Created with IntelliJ IDEA.
 * User: Shashi Mourya
 * Email: 0905shashi@gmail.com
 * Date: 3/18/18
 * Time: 2:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class EvenOdd extends Thread {
    volatile static int i = 1;
    Object lock;

    EvenOdd(Object lock) {
        this.lock = lock;
    }

    public static void main(String ar[]) {
        Object obj = new Object();
        // This constructor is required for the identification of wait/notify
        // communication
        EvenOdd odd = new EvenOdd(obj);
        EvenOdd even = new EvenOdd(obj);
        odd.setName("Odd");
        even.setName("Even");
        odd.start();
        even.start();
    }

    @Override
    public void run() {
        while (i < 11) {
            if (i % 2 == 0 && Thread.currentThread().getName().equals("Even")) {
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName() + " - "
                            + i);
                    i++;
                  //  try {
                        lock.notify();
                   // } catch (InterruptedException e) {
                   //     e.printStackTrace();
                   // }
                }
            }
            if (i % 2 == 1 && Thread.currentThread().getName().equals("Odd")) {
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName() + " - "
                            + i);
                    i++;
                    try{
                    lock.wait();
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
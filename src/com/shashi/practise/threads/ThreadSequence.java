package com.shashi.practise.threads;

/**
 * Created with IntelliJ IDEA.
 * User: Shashi Mourya
 * Date: 3/17/18
 * Time: 8:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThreadSequence implements Runnable{

    volatile int i=0;

    ThreadSequence(){
        new Thread(this).start();
    }

    public void run(){

        while(true){
            synchronized(this){
                try{
                    wait();
                } catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" i="+this.i);
                ++this.i;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException{

        ThreadSequence[] threads = new ThreadSequence[3];

        for(int i=0; i<3; i++){
            threads[i] = new ThreadSequence();
        }

        while(true){

            for(int i=0;i<3;i++){
                synchronized(threads[i]){
                   threads[i].notify();
                    Thread.sleep(1000);
                }
            }
        }

    }
}

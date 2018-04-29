package com.tzj.garvel.playground.javacompiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadsafeCounter {
    private static final Random random = new Random();
    private static final List<Integer> numbers = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Thread t = new Thread(new CounterClient(i, counter));
            threads.add(t);
        }

        for (int i = 0; i < 20; i++) {
            threads.get(i).start();
        }

        for (Thread t : threads) {
            t.join();
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Collections.sort(numbers);
                System.out.println(numbers.size());
                System.out.println(numbers);
                System.out.printf("Duplicates: %b", checkDups(numbers));
            }

            private Object checkDups(final List<Integer> numbers) {
                for (int i = 0; i < numbers.size() - 1; i++) {
                    if (numbers.get(i) == numbers.get(i + 1)) {
                        return true;
                    }
                }

                return false;
            }
        });
    }

    static class Counter {
        private AtomicInteger counter;

        public Counter() {
            this.counter = new AtomicInteger(0);
        }

        public int getNextId() {
            return counter.incrementAndGet();
        }
    }

    static class CounterClient implements Runnable {
        private int id;
        private Counter counter;

        public CounterClient(final int id, final Counter counter) {
            this.id = id;
            this.counter = counter;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(random.nextInt(500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.printf("Thread %d is running\n", id);
            int value = counter.getNextId();
            numbers.add(value);
        }
    }
}

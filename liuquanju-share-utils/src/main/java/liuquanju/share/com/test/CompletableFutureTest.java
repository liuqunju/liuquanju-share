package liuquanju.share.com.test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2020/12/27/20:29
 */
public class CompletableFutureTest {
    AtomicInteger i = new AtomicInteger(0);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFutureTest completableFutureTest = new CompletableFutureTest();
        String a = "a";
        CompletableFuture future1 = CompletableFuture.supplyAsync(() -> completableFutureTest.getStr(a));
        CompletableFuture future2 = CompletableFuture.supplyAsync(() -> CompletableFutureTest1.getString(a));
        CompletableFuture.allOf(future1, future2).join();
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000);
        System.out.println(future1.get());
        System.out.println(future2.get());

    }

    public String getStr(String str) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return str + i.getAndIncrement();
    }
}

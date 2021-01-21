package liuquanju.share.com.test;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2020/12/27/20:42
 */
public class CompletableFutureTest1 {
    public static int getString(String str) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }
}

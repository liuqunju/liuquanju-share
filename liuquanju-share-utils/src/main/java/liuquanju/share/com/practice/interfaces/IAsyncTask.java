package liuquanju.share.com.practice.interfaces;

import java.util.concurrent.Callable;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2020/12/24/21:44
 */
public interface IAsyncTask<T> extends Callable<T> {
}

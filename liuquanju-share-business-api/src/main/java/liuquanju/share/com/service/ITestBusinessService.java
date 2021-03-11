package liuquanju.share.com.service;


/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2021/03/04/9:35
 */
public interface ITestBusinessService<T> {

    /**
     * 发送消息
     * @param msg
     */
    void testRedisMq(T msg);
}

package liuquanju.share.com.redis.mq;

import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2020/05/23/14:13
 */
public class RedisInit {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.11.131", 6381);
        jedis.hset("user", "userName", "zhangsan");
        Set set = jedis.keys("*");
        String user = jedis.hget("user", "userName");
        System.out.println(user);
    }
}

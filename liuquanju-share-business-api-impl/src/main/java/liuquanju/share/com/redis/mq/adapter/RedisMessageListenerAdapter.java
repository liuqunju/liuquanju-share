package liuquanju.share.com.redis.mq.adapter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2021/03/02/15:33
 */
@Configuration
public class RedisMessageListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisMessageListenerAdapter.class);

    public RedisMessageListenerContainer container(
        @Qualifier("jedisConnectionFactory") RedisConnectionFactory redisConnectionFactory,
        List<MessageListenerAdapter> adapters) {
        return null;
    }
}

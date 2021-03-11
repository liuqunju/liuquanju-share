package liuquanju.share.com.redis.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;
import liuquanju.share.com.redis.subscriber.AbstractRedisMqSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.util.CollectionUtils;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2021/03/02/15:33
 */
@Configuration
public class RedisMessageListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisMessageListenerAdapter.class);

    @Bean
    public RedisMessageListenerContainer container(
        @Qualifier("jedisConnectionFactory") RedisConnectionFactory redisConnectionFactory,
        List<MessageListenerAdapter> adapters) {
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(redisConnectionFactory);

        for (MessageListenerAdapter adapter : adapters) {
            AbstractRedisMqSubscriber delegate = (AbstractRedisMqSubscriber)adapter.getDelegate();
            listenerContainer.addMessageListener(delegate, new PatternTopic(delegate.subscribeTopic()));

        }
        return listenerContainer;
    }

    @Bean
    public List<MessageListenerAdapter> adapters(List<MessageListener> listeners) {

        if (CollectionUtils.isEmpty(listeners)) {
            return new ArrayList<>();
        }

        List<MessageListenerAdapter> adapters = new ArrayList<>();
        for (MessageListener listener : listeners) {
            adapters.add(new MessageListenerAdapter(listener, "onMessage"));
        }

        return adapters;

    }
}

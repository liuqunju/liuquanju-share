package liuquanju.share.com.redis.mq.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2021/03/03/17:05
 */
public abstract class AbstractRedisMqSubscriber implements MessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRedisMqSubscriber.class);

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 获取topic主题
     * @return
     */
    public abstract String subscribeTopic();

    /**
     * 订阅
     * @param message 消息
     */
    protected abstract void subscribe(Object message);

    @Override
    public void onMessage(Message message, byte[] bytes) {
        Object msgBody = redisTemplate.getDefaultSerializer().deserialize(message.getBody());
        subscribe(msgBody);
    }
}

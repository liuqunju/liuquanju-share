package liuquanju.share.com.redis.publis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2021/03/03/17:41
 */
@Component
public class RedisMqPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisMqPublisher.class);

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 发送mq消息
     * @param topic 主题
     * @param message 消息
     */
    public void publish(String topic, Object message) {
        redisTemplate.convertAndSend(topic, message);
        LOGGER.info("================ redis mq publish success, topic:{} --- msg:{}", topic, message);

    }
}

package liuquanju.share.com.redis.mq.queue;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import liuquanju.share.com.redis.mq.constant.RedisMqConstant;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2021/03/03/18:00
 */
@Component
public class QueueOps {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueOps.class);

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 消息入队
     * @param topic mq主题
     * @param message 消息
     */
    public void acquireQueued(String topic, Object message){
        try {
            redisTemplate.opsForList().leftPush(RedisMqConstant.QUEUED_PREFIX + topic, message);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }
}

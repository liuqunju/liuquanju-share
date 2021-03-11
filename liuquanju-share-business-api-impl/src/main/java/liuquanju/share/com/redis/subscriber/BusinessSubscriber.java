package liuquanju.share.com.redis.subscriber;

import javax.annotation.Resource;

import liuquanju.share.com.redis.queue.QueueOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liuquanju.share.com.redis.constant.RedisMqConstant;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2021/03/03/18:03
 */
@Component
public class BusinessSubscriber extends AbstractRedisMqSubscriber{
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessSubscriber.class);

    @Resource
    private QueueOps queueOps;

    @Override
    public String subscribeTopic() {
        return RedisMqConstant.TEST_TOPIC;
    }

    @Override
    protected void subscribe(Object message) {
        LOGGER.info("================= BusinessSubscriber subscribe ==================");
        queueOps.acquireQueued(RedisMqConstant.QUEUED_PREFIX + subscribeTopic(), message);
        LOGGER.info("================= BusinessSubscriber acquireQueued success, body:{}", message);
    }
}

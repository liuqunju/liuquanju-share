package liuquanju.share.com.redis.mq.subscriber;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import liuquanju.share.com.redis.mq.constant.RedisMqConstant;
import liuquanju.share.com.redis.mq.queue.QueueOps;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2021/03/03/18:03
 */
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
        queueOps.acquireQueued(subscribeTopic(), message);
        LOGGER.info("================= BusinessSubscriber acquireQueued success, body:{}", message);
    }
}

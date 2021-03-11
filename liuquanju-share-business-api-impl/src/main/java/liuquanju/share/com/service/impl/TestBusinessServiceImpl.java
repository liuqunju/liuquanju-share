package liuquanju.share.com.service.impl;

import liuquanju.share.com.redis.constant.RedisMqConstant;
import liuquanju.share.com.redis.publis.RedisMqPublisher;
import liuquanju.share.com.service.ITestBusinessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2021/03/04/11:14
 */
@Service
public class TestBusinessServiceImpl implements ITestBusinessService {

    @Resource
    private RedisMqPublisher redisMqPublisher;

    @Override
    public void testRedisMq(Object msg) {
        redisMqPublisher.publish(RedisMqConstant.TEST_TOPIC, msg);
    }
}

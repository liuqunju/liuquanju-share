package liuquanju.share.com.redis.subscriber;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.SerializationException;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2021/03/03/17:05
 */
public abstract class AbstractRedisMqSubscriber implements MessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRedisMqSubscriber.class);

    /**
     * 获取topic主题
     * 
     * @return
     */
    public abstract String subscribeTopic();

    /**
     * 订阅
     * 
     * @param message
     *            消息
     */
    protected abstract void subscribe(Object message);

    @Override
    public void onMessage(Message message, byte[] bytes) {
        Object msgBody = deserialize(message.getBody());
        subscribe(msgBody);
    }

    public <T> T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }

        return (T)JSONObject.parse(bytes);
    }
}

package liuquanju.share.com.mq.orderproducer;

import com.alibaba.fastjson.JSONObject;
import liuquanju.share.com.utils.SpringApplicationContextUtil;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Producer {

    @Bean("testProducer")
    public DefaultMQProducer orderProducer(){
        DefaultMQProducer mqProducer = new DefaultMQProducer("Test_MqProducerGroup");
        mqProducer.setNamesrvAddr("127.0.0.1:9876");
        try {
            mqProducer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return mqProducer;
    }
    public static void main(String[] args) {

        DefaultMQProducer testProducer = new DefaultMQProducer("Test_MqProducerGroup");
        testProducer.setNamesrvAddr("127.0.0.1:9876");
        try {
            testProducer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

//        DefaultMQProducer testProducer = (DefaultMQProducer) SpringApplicationContextUtil.getBean("testProducer");
        for (int i = 0; i < 10; i++) {
            Message message = new Message("testTopic", "testTags", ("消息" + i).toString().getBytes());
            try {
                SendResult result = testProducer.send(message, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                        Integer id = (Integer) o;
                        return list.get(id);
                    }
                }, 0);
                System.out.println(JSONObject.toJSONString(result));

            } catch (MQClientException e) {
                e.printStackTrace();
            } catch (RemotingException e) {
                e.printStackTrace();
            } catch (MQBrokerException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 10; i++) {
            Message message = new Message("testTopic", "testTags", ("消息" + i).toString().getBytes());
            try {
                SendResult result = testProducer.send(message, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                        Integer id = (Integer) o;
                        return list.get(id);
                    }
                }, 1);
                System.out.println(JSONObject.toJSONString(result));

            } catch (MQClientException e) {
                e.printStackTrace();
            } catch (RemotingException e) {
                e.printStackTrace();
            } catch (MQBrokerException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        testProducer.shutdown();
    }

}

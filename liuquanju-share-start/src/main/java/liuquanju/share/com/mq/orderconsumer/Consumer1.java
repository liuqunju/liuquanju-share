package liuquanju.share.com.mq.orderconsumer;

import liuquanju.share.com.listener.MqOrderListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class Consumer1 {
    @Bean("testConsumer")
    public DefaultMQPushConsumer mQPullConsumer(){
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("Test_MqProducerGroup");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.setInstanceName(UUID.randomUUID().toString());
        try {
            consumer.subscribe("testTopic", "*");
            consumer.registerMessageListener(new MqOrderListener());
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return consumer;
    }

    public static void main(String[] args) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("Test_MqProducerGroup");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.setInstanceName(UUID.randomUUID().toString());
        try {
            consumer.subscribe("testTopic", "*");
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            consumer.registerMessageListener(new MqOrderListener());
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

        System.out.println("消费者启动。。。");
    }
}

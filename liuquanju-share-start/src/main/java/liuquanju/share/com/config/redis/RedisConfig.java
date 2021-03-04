package liuquanju.share.com.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2021/03/02/16:00
 */
@Configuration
@EnableConfigurationProperties({Single.class})
public class RedisConfig extends CachingConfigurerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);
    private static final String SINGLE = "single";

    @Autowired
    private Single single;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("jedisConnectionFactory") JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }


    @RefreshScope
    @Bean
    public KeyGenerator wiselyKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    @RefreshScope
    @Bean(value = "jedisConnectionFactory")
    public JedisConnectionFactory redisConnectionFactory() {
        return buildSingleJedisConnectionFactory();
    }


    private <T> T defaultValue(T target, T defaultVaue) {
        return target == null ? defaultVaue : target;
    }

    private JedisConnectionFactory buildSingleJedisConnectionFactory() {
        LOGGER.info("============= redis: single ==================");

        LOGGER.info("============= redis host:{}", single.getHost());
        LOGGER.info("============= redis port:{}", single.getPort());
        LOGGER.info("============= redis pwd:{}", single.getPassword());
        LOGGER.info("============= redis poolMaxWait:{}", single.getPoolMaxWait());

        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration();
        standaloneConfiguration.setHostName("".equals(single.getHost()) ? "127.0.0.1" : single.getHost());
        standaloneConfiguration.setPort(null == single.getPort() ? 6379 : single.getPort());
        if (StringUtils.isNotBlank(single.getPassword())) {
            standaloneConfiguration.setPassword(RedisPassword.of(single.getPassword().trim()));
        }
        standaloneConfiguration.setDatabase(null == single.getData() ? 0 : single.getData());
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(defaultValue(single.getPoolMaxActive(), GenericObjectPoolConfig.DEFAULT_MAX_TOTAL));
        jedisPoolConfig.setMaxIdle(defaultValue(single.getPoolMaxIdle(), GenericObjectPoolConfig.DEFAULT_MAX_IDLE));
        jedisPoolConfig.setMinIdle(defaultValue(single.getPoolMinIdle(), GenericObjectPoolConfig.DEFAULT_MIN_IDLE));
        jedisPoolConfig.setMaxWaitMillis(defaultValue(single.getPoolMaxWait(),
                GenericObjectPoolConfig.DEFAULT_MAX_WAIT_MILLIS));

        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jedisClientConfigurationBuilder =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        jedisClientConfigurationBuilder.poolConfig(jedisPoolConfig);

        return new JedisConnectionFactory(standaloneConfiguration, jedisClientConfigurationBuilder.build());
    }

}

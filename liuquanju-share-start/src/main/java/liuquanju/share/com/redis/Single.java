package liuquanju.share.com.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2021/03/02/16:16
 */
@Data
@ConfigurationProperties(prefix = "redis.single")
public class Single {


    private String host;
    private Integer port;
    private String password;
    private Integer poolMaxActive;
    private Long poolMaxWait;
    private Integer poolMaxIdle;
    private Integer poolMinIdle;
    private Long timeout;
    private Integer data;

}

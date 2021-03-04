package liuquanju.share.com;

import liuquanju.share.com.utils.SpringApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LiuquanjuShareApplicationStart extends SpringBootServletInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(LiuquanjuShareApplicationStart.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return super.configure(builder);
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(LiuquanjuShareApplicationStart.class, args);
        SpringApplicationContextUtil.setApplicationContext(context);
        LOGGER.info("=========== push context into env ===============");
    }
}

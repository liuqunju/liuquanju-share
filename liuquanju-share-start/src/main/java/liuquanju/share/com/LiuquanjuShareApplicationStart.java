package liuquanju.share.com;

import liuquanju.share.com.utils.SpringApplicationContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class LiuquanjuShareApplicationStart extends SpringBootServletInitializer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return super.configure(builder);
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(LiuquanjuShareApplicationStart.class, args);
        SpringApplicationContextUtil.setApplicationContext(context);
        System.out.println("已经将context放入");
    }
}

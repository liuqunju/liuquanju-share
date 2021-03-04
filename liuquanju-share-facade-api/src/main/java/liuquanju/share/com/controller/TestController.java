package liuquanju.share.com.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2021/03/02/11:42
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/ok")
    public String test() {
        return "ok";
    }
}

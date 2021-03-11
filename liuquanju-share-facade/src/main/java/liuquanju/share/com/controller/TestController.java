package liuquanju.share.com.controller;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2021/03/04/10:22
 */

import liuquanju.share.com.service.ITestBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: yingjie.liu
 * @Date: 2021/03/02/11:42
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ITestBusinessService iTestBusinessService;

    @RequestMapping("/ok")
    public String test() {
        Map map = new HashMap();
        map.put("msg", "hello redis mq test");
        iTestBusinessService.testRedisMq(map);
        return "ok";
    }
}
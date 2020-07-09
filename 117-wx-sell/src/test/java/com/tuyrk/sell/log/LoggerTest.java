package com.tuyrk.sell.log;

import com.tuyrk.sell.ApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/9 12:16 星期三
 * Description:
 */
@Slf4j
public class LoggerTest extends ApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void test1() {
        String name = "imooc";
        String password = "123456";
        logger.debug("debug...");
        logger.info("info...");
        logger.error("error...");

        log.info("name: " + name + ", password: " + password);
        log.info("name: {}, password: {}", name, password);
        log.info("name: {}, password: {}", name);
        log.info("name: {}, password: {}", name, password, name);

        log.warn("warn...");
    }
}

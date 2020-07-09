package com.tuyrk.kafka.web;

import com.tuyrk.kafka.entity.AccurateWatcherMessage;
import com.tuyrk.kafka.util.InputMDC;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tuyrk
 */
@Slf4j
@RestController
@RequestMapping("index")
public class IndexController {
    /**
     * [%d{yyyy-MM-dd'T'HH:mm:ss.SSSZZ}]
     * [%level{length=5}]
     * [%thread-%tid]
     * [%logger]
     * [%X{hostName}]
     * [%X{ip}]
     * [%X{applicationName}]
     * [%F, %L, %C, %M]
     * [%m]
     * ## '%ex'%n
     * <p>
     * ["message", "
     * \[%{NOTSPACE:currentDateTime}\]
     * \[%{NOTSPACE:level}\]
     * \[%{NOTSPACE:thread-id}\]
     * \[%{NOTSPACE:class}\]
     * \[%{DATA:hostName}\]
     * \[%{DATA:ip}\]
     * \[%{DATA:applicationName}\]
     * \[%{DATA:location}\]
     * \[%{DATA:messageInfo}\]
     * ## (\'\'|%{QUOTEDSTRING:throwable})
     * "]
     * ["message", "\[%{NOTSPACE:currentDateTime}\] \[%{NOTSPACE:level}\] \[%{NOTSPACE:thread-id}\] \[%{NOTSPACE:class}\] \[%{DATA:hostName}\] \[%{DATA:ip}\] \[%{DATA:applicationName}\] \[%{DATA:location}\] \[%{DATA:messageInfo}\] ## (\'\'|%{QUOTEDSTRING:throwable})"]
     *
     * @return
     */
    @GetMapping("")
    public String index() {
        // MDC
        InputMDC.putMDC();
        log.info("我是一条info日志.");
        log.warn("我是一条warn日志.");
        log.error("我是一条error日志.");
        return "idx";
    }

    @GetMapping("err")
    public String err() {
        InputMDC.putMDC();
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            log.error("算术异常", e);
        }
        return "err";
    }

    @PostMapping("accurateWatch")
    public String accurateWatch(AccurateWatcherMessage accurateWatcherMessage) {
        System.out.println("-------警告内容-------" + accurateWatcherMessage);
        return "is watched" + accurateWatcherMessage;
    }
}
/*

 */

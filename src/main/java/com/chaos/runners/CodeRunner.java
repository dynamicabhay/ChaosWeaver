package com.chaos.runners;

import com.chaos.service.DemoService;
import com.chaos.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CodeRunner implements CommandLineRunner {
    @Autowired
    DemoService demoService;

    @Autowired
    OrderService orderService;
    @Override
    public void run(String... args) throws Exception {
        try {
            demoService.chaosMethod1();

        } catch (Exception e) {
            log.error(e.getMessage());

        }

        try {
            demoService.chaosMethod2();

        } catch (Exception e) {
            log.error(e.getMessage());
            //throw new RuntimeException(e.getMessage());
        }

        try {
            demoService.processOrder();

        } catch (Exception e) {
            log.error(e.getMessage());
            //throw new RuntimeException(e.getMessage());
        }


        try {
            orderService.getOrder();

        } catch (Exception e) {
            log.error(e.getMessage());
            //throw new RuntimeException(e.getMessage());
        }


    }
}

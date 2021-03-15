package com.infogain.gcp.poc.component;

import java.time.LocalTime;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.infogain.gcp.poc.poller.service.PnrService;

@Slf4j
@Component
public class PollerExecutor {

    private PnrService pnrService;

    @Autowired
    public PollerExecutor(PnrService pnrService) {
        this.pnrService = pnrService;
    }

    // @Scheduled(cron = "*/10 * * * * *")
    @Scheduled(cron = "0 * * * * *")
    public void process() {
        log.info("poller started at {}", LocalTime.now());
        pnrService.execute();
        log.info("poller completed at {}", LocalTime.now());
    }
}

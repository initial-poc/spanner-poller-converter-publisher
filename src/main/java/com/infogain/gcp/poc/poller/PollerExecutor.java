package com.infogain.gcp.poc.poller;

import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.infogain.gcp.poc.poller.service.PnrService;

@Component
public class PollerExecutor {
private static final Logger log= LoggerFactory.getLogger(PollerExecutor.class);

private PnrService pnrService;

@Autowired
public PollerExecutor(PnrService pnrService) {
	this.pnrService = pnrService;
}

	
@Scheduled(cron = "*/10 * * * * *")
	public void process() {
		log.info("poller started at {}",LocalTime.now());
		pnrService.getUpdatedPnrDetails();
		log.info("poller task completed at {}",LocalTime.now());
	}
}

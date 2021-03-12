package com.infogain.gcp.poc.poller.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.Timestamp;
import com.infogain.gcp.poc.App.PubsubOutboundGateway;
import com.infogain.gcp.poc.convertor.MessageConvertor;
import com.infogain.gcp.poc.poller.entity.PNR;
import com.infogain.gcp.poc.poller.repository.PnrRepository;
import com.infogain.gcp.poc.poller.repository.SpannerCommitTimestampRepository;

@Service
public class PnrService {
	private static final Logger logger = LoggerFactory.getLogger(PnrService.class);
	private final SpannerCommitTimestampRepository spannerCommitTimestampRepository;
	private final PnrRepository pnrRepository;
	private final MessageConvertor messageConvertor;
	private final PubsubOutboundGateway messagingGateway;

	@Autowired
	public PnrService(SpannerCommitTimestampRepository spannerCommitTimestampRepository, PnrRepository pnrRepository,
			MessageConvertor messageConvertor, PubsubOutboundGateway messagingGateway) {
		super();
		this.spannerCommitTimestampRepository = spannerCommitTimestampRepository;
		this.pnrRepository = pnrRepository;
		this.messageConvertor = messageConvertor;
		this.messagingGateway = messagingGateway;
	}

	public void getUpdatedPnrDetails() {
		Timestamp timestamp = spannerCommitTimestampRepository.getPollerCommitTimestamp();
		List<PNR> pnrs = pnrRepository.getPnrDetailToProcess(timestamp);
		
		try {
			processMessage(pnrs);
		}catch(Exception ex) {
			logger.info("Got exception while publishing the message {}",ex);
		}finally {
			Timestamp lastRecordUpdatedTimestamp=null;
			if(pnrs.isEmpty()) {
				lastRecordUpdatedTimestamp = spannerCommitTimestampRepository.getCurrentTimestamp();
			}else {
				  lastRecordUpdatedTimestamp=	getLastRecordUpdatedTimestamp(pnrs);
			}
			
			logger.info("Going to save the poller last execution time into db {}",lastRecordUpdatedTimestamp);
			spannerCommitTimestampRepository.setPollerLastTimestamp(lastRecordUpdatedTimestamp);
		}
		
		
		
	}

	private void processMessage(List<PNR> pnrs) {
		pnrs.stream().forEach(pnr -> publishMessage(messageConvertor.doConvert(pnr)));

	}

	private void publishMessage(String message) {
		messagingGateway.sendToPubsub(message);
	}
	
	private Timestamp getLastRecordUpdatedTimestamp(List<PNR> pnrs) {
		return pnrs.get(pnrs.size()-1).getLastUpdateTimestamp();
	}
}

package com.infogain.gcp.poc.poller.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.stereotype.Service;

import com.google.cloud.Timestamp;
import com.infogain.gcp.poc.component.MessageConverter;
import com.infogain.gcp.poc.poller.entity.PNR;
import com.infogain.gcp.poc.poller.repository.PnrRepository;
import com.infogain.gcp.poc.poller.repository.SpannerCommitTimestampRepository;

@Slf4j
@Service
public class PnrService {

	private final SpannerCommitTimestampRepository spannerCommitTimestampRepository;
	private final PnrRepository pnrRepository;
	private final MessageConverter messageConverter;
	private final PubSubTemplate pubSubTemplate;

	@Value("${app.topic.name}")
	private String topicName;

	@Autowired
	public PnrService(SpannerCommitTimestampRepository spannerCommitTimestampRepository,
					  PnrRepository pnrRepository,
					  MessageConverter messageConverter,
					  PubSubTemplate pubSubTemplate) {
		this.spannerCommitTimestampRepository = spannerCommitTimestampRepository;
		this.pnrRepository = pnrRepository;
		this.messageConverter = messageConverter;
		this.pubSubTemplate = pubSubTemplate;
	}

	public void execute() {
		Timestamp timestamp = spannerCommitTimestampRepository.getPollerCommitTimestamp();
		List<PNR> pnrs = ListUtils.emptyIfNull(pnrRepository.getPnrDetailToProcess(timestamp));

		try {
			processMessage(pnrs);
		} catch (Exception ex) {
			log.info("Got exception while publishing the message", ex);
		} finally {
			Timestamp lastRecordUpdatedTimestamp = null;
			if (pnrs.isEmpty()) {
				lastRecordUpdatedTimestamp = spannerCommitTimestampRepository.getCurrentTimestamp();
			} else {
				lastRecordUpdatedTimestamp = getLastRecordUpdatedTimestamp(pnrs);
			}

			log.info("Going to save the poller last execution time into db {}", lastRecordUpdatedTimestamp);
			spannerCommitTimestampRepository.setPollerLastTimestamp(lastRecordUpdatedTimestamp);
		}

	}

	private void processMessage(List<PNR> pnrs) {
		pnrs.forEach(pnr -> publishMessage(messageConverter.convert(pnr)));
	}

	private void publishMessage(String message) {
		log.info("publishing message {} to topic {}", message, topicName);
		pubSubTemplate.publish(topicName, message);
		log.info("published message {} to topic {}", message, topicName);
	}

	private Timestamp getLastRecordUpdatedTimestamp(List<PNR> pnrs) {
		return pnrs.get(pnrs.size() - 1).getLastUpdateTimestamp();
	}

}
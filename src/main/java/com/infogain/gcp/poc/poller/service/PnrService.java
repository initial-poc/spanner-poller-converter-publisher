package com.infogain.gcp.poc.poller.service;

import java.util.List;

import com.infogain.gcp.poc.poller.entity.PNREntity;
import com.infogain.gcp.poc.poller.entity.PollerCommitTimestamp;
import com.infogain.gcp.poc.poller.repository.PNREntityRepository;
import com.infogain.gcp.poc.poller.repository.PollerCommitTimestampRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.stereotype.Service;

import com.google.cloud.Timestamp;
import com.infogain.gcp.poc.component.MessageConverter;
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
	private PollerCommitTimestampRepository pollerCommitTimestampRepository;

	@Autowired
	private PNREntityRepository pnrEntityRepository;

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

	public List<PNREntity> getPnrDetailToProcessV2(Timestamp timestamp) {
		List<PNREntity> pnrEntities = null;

		if (timestamp == null) {
			log.info("Last commit timestamp is null in table so getting all pnr records from db");
//			statement = Statement.of(ApplicationConstant.POLL_QUERY_WITHOUT_TIMESTAMP);
			pnrEntities = pnrEntityRepository.findAllByOrderByLastUpdateTimestamp();
		} else {
			log.info("Getting all the PNR after timestamp {}", timestamp);
//			statement = Statement.newBuilder(ApplicationConstant.POLL_QUERY_WITH_TIMESTAMP)
//					.bind(ApplicationConstant.PREVIOUS_TIMESTAMP_PLACE_HOLDER).to(timestamp).build();
			pnrEntities = pnrEntityRepository.findByLastUpdateTimestampGreaterThanOrderByLastUpdateTimestamp(timestamp);
		}

//		List<PNREntity> pnrEntities =  spannerGateway.getAllRecord(statement, PNREntity.class);
		log.info("Total PNR Found {}", pnrEntities.size());
		log.info("PNR RECORDS ARE  {}", pnrEntities);
		return pnrEntities;
	}

	public void execute() {

		PollerCommitTimestamp pollerCommitTimestamp = pollerCommitTimestampRepository.findFirstByOrderByLastCommitTimestamp();
		log.info("pollerCommitTimestamp={}", pollerCommitTimestamp);

		Timestamp timestamp = pollerCommitTimestamp.getLastCommitTimestamp(); // spannerCommitTimestampRepository.getPollerCommitTimestamp();
		List<PNREntity> pnrEntities = ListUtils.emptyIfNull(getPnrDetailToProcessV2(timestamp));

		try {
			processMessage(pnrEntities);
		} catch (Exception ex) {
			log.info("Got exception while publishing the message", ex);
		} finally {
			Timestamp lastRecordUpdatedTimestamp = null;
			if (pnrEntities.isEmpty()) {
				lastRecordUpdatedTimestamp = spannerCommitTimestampRepository.getCurrentTimestamp();
			} else {
				lastRecordUpdatedTimestamp = getLastRecordUpdatedTimestamp(pnrEntities);
			}

			log.info("Going to save the poller last execution time into db {}", lastRecordUpdatedTimestamp);

			if(!timestamp.equals(lastRecordUpdatedTimestamp)){
				spannerCommitTimestampRepository.setPollerLastTimestamp(lastRecordUpdatedTimestamp);
			}else{
				log.info("last record updated timestamp already updated. So not inserting again");
			}


		}

	}

	private void processMessage(List<PNREntity> pnrEntities) {
		pnrEntities.forEach(pnrEntity -> publishMessage(messageConverter.convert(pnrEntity)));
	}

	private void publishMessage(String message) {
		log.info("publishing message {} to topic {}", message, topicName);
		pubSubTemplate.publish(topicName, message);
		log.info("published message {} to topic {}", message, topicName);
	}

	private Timestamp getLastRecordUpdatedTimestamp(List<PNREntity> pnrEntities) {
		return pnrEntities.get(pnrEntities.size() - 1).getLastUpdateTimestamp();
	}

}
package com.infogain.gcp.poc.poller.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.google.cloud.Timestamp;
import com.google.cloud.spanner.Statement;
import com.infogain.gcp.poc.constant.ApplicationConstant;
import com.infogain.gcp.poc.poller.gateway.SpannerGateway;

@Repository
public class SpannerCommitTimestampRepository {
	private static final Logger logger= LoggerFactory.getLogger(SpannerCommitTimestampRepository.class);

	@Value("${poller.commit.table.name}")
	private String pollerCommitTableName;

	private SpannerGateway spannerGateway;

	@Autowired
	public SpannerCommitTimestampRepository(SpannerGateway spannerGateway) {
		this.spannerGateway = spannerGateway;
	}

	public Timestamp getPollerCommitTimestamp() {
		Timestamp timestamp = null;
		timestamp = spannerGateway
				.getTimestampRecord(Statement.of(ApplicationConstant.LAST_COMIMT_TIMESTAMP_QUERY));
		if (timestamp == null) {
			logger.info("no timestamp in POLLER_COMMIT_TIMESTAMPS table");
			timestamp = getCurrentTimestamp();

		}

		logger.info("got timestamp {}", timestamp);
		return timestamp;

	}
	
	public Timestamp getCurrentTimestamp() {
	return spannerGateway.getTimestampRecord(Statement.of(ApplicationConstant.CURRENT_TIMESTAMP_QUERY));	
	}
	
	
	public void setPollerLastTimestamp(Timestamp pollerLastExecutedTimestamp) {
		logger.info("going to save the poller last executed timestamp {}", pollerLastExecutedTimestamp);
		 Statement statement =  Statement.newBuilder(   ApplicationConstant.LAST_COMIMT_TIMESTAMP_SAVE_QUERY).bind(ApplicationConstant.LAST_POLLER_EXECUTION_TIMESTAMP_PLACE_HOLDER).to(pollerLastExecutedTimestamp).build();
		spannerGateway.save(statement);
	}

}

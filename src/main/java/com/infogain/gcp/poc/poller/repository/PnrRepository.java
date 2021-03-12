package com.infogain.gcp.poc.poller.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.cloud.Timestamp;
import com.google.cloud.spanner.Statement;
import com.infogain.gcp.poc.constant.ApplicationConstant;
import com.infogain.gcp.poc.poller.entity.PNR;
import com.infogain.gcp.poc.poller.gateway.SpannerGateway;

@Repository
public class PnrRepository {
	private static final Logger logger = LoggerFactory.getLogger(PnrRepository.class);
	private SpannerGateway spannerGateway;

	@Autowired
	public PnrRepository(SpannerGateway spannerGateway) {
		this.spannerGateway = spannerGateway;
	}

	public List<PNR> getPnrDetailToProcess(Timestamp timestamp) {
		Statement statement = null;
		if (timestamp == null) {
			logger.info("Last commit timestamp is null in table so getting all pnr records from db");
			statement = Statement.of(ApplicationConstant.POLL_QUERY_WITHOUT_TIMESTAMP);
		} else {
			logger.info("Getting all the PNR after timestamp {}", timestamp);
			statement = Statement.newBuilder(ApplicationConstant.POLL_QUERY_WITH_TIMESTAMP)
					.bind(ApplicationConstant.PREVIOUS_TIMESTAMP_PLACE_HOLDER).to(timestamp).build();
		}
		

		List<PNR> pnrs = spannerGateway.getAllRecord(statement, PNR.class);
		logger.info("Total PNR Found {}", pnrs.size());
		logger.info("PNR RECORDS ARE  {}", pnrs);
		return pnrs;
	}

}

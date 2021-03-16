package com.infogain.gcp.poc.poller.repository;

import java.util.List;

import com.infogain.gcp.poc.poller.entity.PNREntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.cloud.Timestamp;
import com.google.cloud.spanner.Statement;
import com.infogain.gcp.poc.util.ApplicationConstant;
import com.infogain.gcp.poc.component.SpannerGateway;

@Slf4j
@Repository
public class PnrRepository {

	private SpannerGateway spannerGateway;

	@Autowired
	public PnrRepository(SpannerGateway spannerGateway) {
		this.spannerGateway = spannerGateway;
	}

	public List<PNREntity> getPnrDetailToProcess(Timestamp timestamp) {
		Statement statement = null;
		if (timestamp == null) {
			log.info("Last commit timestamp is null in table so getting all pnr records from db");
			statement = Statement.of(ApplicationConstant.POLL_QUERY_WITHOUT_TIMESTAMP);
		} else {
			log.info("Getting all the PNR after timestamp {}", timestamp);
			statement = Statement.newBuilder(ApplicationConstant.POLL_QUERY_WITH_TIMESTAMP)
					.bind(ApplicationConstant.PREVIOUS_TIMESTAMP_PLACE_HOLDER).to(timestamp).build();
		}

		List<PNREntity> pnrEntities =  spannerGateway.getAllRecord(statement, PNREntity.class);
		log.info("Total PNR Found {}", pnrEntities.size());
		log.info("PNR RECORDS ARE  {}", pnrEntities);
		return pnrEntities;
	}



}

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
	private static final Logger logger= LoggerFactory.getLogger(PnrRepository.class);
	private SpannerGateway spannerGateway;
	
@Autowired	
	public PnrRepository(SpannerGateway spannerGateway) {
		this.spannerGateway = spannerGateway;
	}


	public List<PNR> getPnrDetailToProcess(Timestamp timestamp) {
	
		logger.info("try to get all the pnr after timestamp {}" ,timestamp);
		 Statement statement =  Statement.newBuilder(   ApplicationConstant.POLL_QUERY).bind(ApplicationConstant.PREVIOUS_TIMESTAMP_PLACE_HOLDER).to(timestamp).build();
		 
		 List<PNR> pnrs=	 spannerGateway.getAllRecord(statement, PNR.class);
		 logger.info("total pnr found {}",pnrs.size());
		 logger.info("got the pnr {}",pnrs);
		 return pnrs;
	}
	
}

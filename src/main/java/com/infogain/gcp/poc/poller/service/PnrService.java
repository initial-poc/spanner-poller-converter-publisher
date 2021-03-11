package com.infogain.gcp.poc.poller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.Timestamp;
import com.infogain.gcp.poc.poller.repository.PnrRepository;
import com.infogain.gcp.poc.poller.repository.SpannerCommitTimestampRepository;

@Service
public class PnrService {

	private SpannerCommitTimestampRepository spannerCommitTimestampRepository;
	private PnrRepository pnrRepository;

	@Autowired
	public PnrService(SpannerCommitTimestampRepository spannerCommitTimestampRepository, PnrRepository pnrRepository) {
		this.spannerCommitTimestampRepository = spannerCommitTimestampRepository;
		this.pnrRepository = pnrRepository;
	}

	public void getUpdatedPnrDetails() {
		Timestamp timestamp = spannerCommitTimestampRepository.getPollerCommitTimestamp();
		pnrRepository.getPnrDetailToProcess(timestamp);
		Timestamp currentTimestamp = spannerCommitTimestampRepository.getCurrentTimestamp();
		spannerCommitTimestampRepository.setPollerLastTimestamp(currentTimestamp);
	}
}

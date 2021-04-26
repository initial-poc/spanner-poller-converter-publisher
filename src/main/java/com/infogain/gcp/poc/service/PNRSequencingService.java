package com.infogain.gcp.poc.service;

import org.springframework.stereotype.Service;

import com.infogain.gcp.poc.component.PNRMessageGroupStore;
import com.infogain.gcp.poc.domainmodel.PNRModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PNRSequencingService {
	
	private final PNRMessageGroupStore messageGroupStore;

	public void processPNR(PNRModel pnrModel) {
		
		messageGroupStore.addMessage(pnrModel);
		
	}
	
}

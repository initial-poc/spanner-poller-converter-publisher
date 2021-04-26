package com.infogain.gcp.poc.component;

import org.springframework.stereotype.Component;

import com.infogain.gcp.poc.domainmodel.PNRModel;
import com.infogain.gcp.poc.entity.PNREntity;
import com.infogain.gcp.poc.poller.repository.GroupMessageStoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PNRMessageGroupStore {
	private final GroupMessageStoreRepository groupMessageStoreRepository;

	private static final String IN_PROGRESS="1";
	private static final String RELEASED ="2";
	
	
	public void addMessage(PNRModel pnrModel) {
		PNREntity pnrEntity = pnrModel.buildEntity(); 
		pnrEntity.setStatus(IN_PROGRESS);
		log.info("saving message {}",pnrEntity);
		
		groupMessageStoreRepository.getSpannerTemplate().insert(pnrEntity);
		
	}

}

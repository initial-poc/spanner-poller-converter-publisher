package com.infogain.gcp.poc.component;

import java.util.List;

import org.springframework.stereotype.Component;

import com.infogain.gcp.poc.domainmodel.PNRModel;
import com.infogain.gcp.poc.entity.PNREntity;
import com.infogain.gcp.poc.poller.repository.GroupMessageStoreRepository;
import com.infogain.gcp.poc.util.AppConstant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PNRMessageGroupStore {
	private final GroupMessageStoreRepository groupMessageStoreRepository;

	public PNREntity addMessage(PNRModel pnrModel) {
		PNREntity pnrEntity = pnrModel.buildEntity();
		pnrEntity.setStatus(AppConstant.IN_PROGRESS);
		log.info("saving message {}", pnrEntity);

		groupMessageStoreRepository.getSpannerTemplate().insert(pnrEntity);
		return pnrEntity;

	}

	public void releaseMessage(List<PNREntity> pnrEntity) {
		log.info("Going to update table as released for messages {}", pnrEntity);

		pnrEntity.stream().forEach(entity -> {
			entity.setStatus(AppConstant.RELEASED);
			groupMessageStoreRepository.getSpannerTemplate().update(entity);
			//groupMessageStoreRepository.save(entity);
		});

	}

}

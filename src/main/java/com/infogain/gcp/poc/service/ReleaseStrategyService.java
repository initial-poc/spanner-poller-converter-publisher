package com.infogain.gcp.poc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.infogain.gcp.poc.entity.PNREntity;
import com.infogain.gcp.poc.poller.repository.GroupMessageStoreRepository;
import com.infogain.gcp.poc.util.AppConstant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReleaseStrategyService {

    private final GroupMessageStoreRepository msgGrpStoreRepository;

    @SuppressWarnings("all")
    public List<PNREntity> release(PNREntity pnrEntity) {
        log.info("Getting all the messages from the table by pnr id");
        Optional<List<PNREntity>> pnrEntityList = msgGrpStoreRepository.findByPnrid(pnrEntity.getPnrid());

        List<PNREntity> pnrList = pnrEntityList.get();
        //	pnrList.add(pnrEntity);
        log.info("Messages are {}", pnrList);
        List<PNREntity> returnList = new ArrayList<PNREntity>();

        Map<Integer, Boolean> seqReleasedStatusMap = pnrList.stream()
                .collect(Collectors.toMap(PNREntity::getMessageseq, x -> x.getStatus().equals(AppConstant.RELEASED) ? true : false));
        Map<Integer, PNREntity> seqPNREntityMap = pnrList.stream()
                .collect(Collectors.toMap(PNREntity::getMessageseq, x -> x));

        if (Optional.ofNullable(seqReleasedStatusMap.get(1)).isPresent()) {
            seqReleasedStatusMap.put(1, true);
            if (!seqPNREntityMap.get(1).getStatus().equals(AppConstant.RELEASED)) {
                returnList.add(seqPNREntityMap.get(1));
            }
        }

        log.info("seqReleasedStatusMap {}", seqReleasedStatusMap);
        log.info("seqPNREntityMap {}", seqPNREntityMap);

        pnrList.stream().sorted().
                filter(x -> Optional.ofNullable(seqReleasedStatusMap.get((x.getMessageseq()) - 1)).isPresent()).
                filter(x -> !seqPNREntityMap.get(x.getMessageseq()).getStatus().equals(AppConstant.RELEASED))
                .forEach(x -> {
                    seqReleasedStatusMap.put(x.getMessageseq(), true);
                    returnList.add(x);
                });

        log.info("returning the list {}", returnList);
        return returnList;
    }

}

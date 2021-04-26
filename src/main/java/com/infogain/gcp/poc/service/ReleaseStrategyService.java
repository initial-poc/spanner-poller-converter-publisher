package com.infogain.gcp.poc.service;

import com.infogain.gcp.poc.entity.PNREntity;
import com.infogain.gcp.poc.poller.repository.GroupMessageStoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReleaseStrategyService {

    @Autowired
    GroupMessageStoreRepository msgGrpStoreRepository;

    @SuppressWarnings("all")
    @Transactional
    public List<PNREntity> release(PNREntity pnrEntity) {
        Optional<List<PNREntity>> pnrEntityList = msgGrpStoreRepository.findByPnrid(pnrEntity.getPnrid());

        List<PNREntity> pnrList = pnrEntityList.get();
        List<PNREntity> returnList = new ArrayList<PNREntity>();

        Map<Integer, Boolean> seqReleasedStatusMap = pnrList.stream().
                collect(Collectors.toMap(PNREntity::getMessageseq, x -> false));
        Map<Integer, PNREntity> seqPNREntityMap = pnrList.stream().
                collect(Collectors.toMap(PNREntity::getMessageseq, x -> x));

        if (Optional.of(seqReleasedStatusMap.get(1)).isPresent()) {
            seqReleasedStatusMap.put(1, true);
            if (!seqPNREntityMap.get(1).getStatus().equals("RELEASED")) {
                returnList.add(seqPNREntityMap.get(1));
            }
        }

        pnrList.stream().sorted().filter(x -> seqPNREntityMap.get(x.getMessageseq() - 1) != null ?
                seqReleasedStatusMap.put(x.getMessageseq(), true) || true : false).
                filter(y -> !y.getStatus().equals("RELEASED")).filter(u ->
                (seqReleasedStatusMap.get(u.getMessageseq() - 1)
                        || seqPNREntityMap.get(u.getMessageseq() - 1).getStatus().equals("RELEASED")
                )).peek(System.out::println).
                forEach(z -> returnList.add(z));

        return returnList;
    }
}

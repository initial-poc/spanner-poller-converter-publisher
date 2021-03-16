package com.infogain.gcp.poc.poller.repository;

import com.google.cloud.Timestamp;
import com.infogain.gcp.poc.poller.entity.PollerCommitTimestamp;
import org.springframework.cloud.gcp.data.spanner.repository.SpannerRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollerCommitTimestampRepository extends SpannerRepository<PollerCommitTimestamp, Timestamp> {

    PollerCommitTimestamp findFirstByOrderByLastCommitTimestamp();

}
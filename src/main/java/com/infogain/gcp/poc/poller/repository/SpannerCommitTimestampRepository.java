package com.infogain.gcp.poc.poller.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.google.cloud.Timestamp;
import com.google.cloud.spanner.Statement;
import com.infogain.gcp.poc.util.ApplicationConstant;
import com.infogain.gcp.poc.component.SpannerGateway;

@Slf4j
@Repository
public class SpannerCommitTimestampRepository {

    @Value("${poller.commit.table.name}")
    private String pollerCommitTableName;

    private SpannerGateway spannerGateway;

    @Autowired
    public SpannerCommitTimestampRepository(SpannerGateway spannerGateway) {
        this.spannerGateway = spannerGateway;
    }

    public Timestamp getPollerCommitTimestamp() {
        Timestamp timestamp = spannerGateway
                .getTimestampRecord(Statement.of(ApplicationConstant.LAST_COMMIT_TIMESTAMP_QUERY));
        log.info("Timestamp in poller commit table {}", timestamp);
        return timestamp;
    }

    public Timestamp getCurrentTimestamp() {
        return spannerGateway.getTimestampRecord(Statement.of(ApplicationConstant.CURRENT_TIMESTAMP_QUERY));
    }

    public void setPollerLastTimestamp(Timestamp pollerLastExecutedTimestamp) {
        log.info("Going to save the poller last executed timestamp {}", pollerLastExecutedTimestamp);
        Statement statement = Statement.newBuilder(ApplicationConstant.LAST_COMMIT_TIMESTAMP_SAVE_QUERY).bind(ApplicationConstant.LAST_POLLER_EXECUTION_TIMESTAMP_PLACE_HOLDER).to(pollerLastExecutedTimestamp).build();
        spannerGateway.save(statement);
    }

}

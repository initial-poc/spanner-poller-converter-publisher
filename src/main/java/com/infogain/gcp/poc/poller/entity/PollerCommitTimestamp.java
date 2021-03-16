package com.infogain.gcp.poc.poller.entity;

import com.google.cloud.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Column;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"lastCommitTimestamp"})
@Table(name = "POLLER_COMMIT_TIMESTAMPS")
public class PollerCommitTimestamp {

    @Column(name = "last_commit_timestamp")
    private Timestamp lastCommitTimestamp;

}

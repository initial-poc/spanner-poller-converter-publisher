package com.infogain.gcp.poc.entity;

import org.springframework.beans.BeanUtils;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Column;
import org.springframework.cloud.gcp.data.spanner.core.mapping.PrimaryKey;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Table;
import org.springframework.data.annotation.Version;

import com.google.cloud.Timestamp;
import com.infogain.gcp.poc.domainmodel.PNRModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "group_message_store")
public class PNREntity implements Comparable{

    @PrimaryKey(keyOrder = 1)
    @Column(name = "pnrid")
    private String pnrid;
@Version
private   long version;
    @PrimaryKey(keyOrder = 2)
    @Column(name = "messageseq")
    private Integer messageseq;

    @PrimaryKey(keyOrder = 3)
    @Column(name = "status")
    private String status;

    @Column(name = "payload")
    private String payload;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @SneakyThrows
    public PNRModel buildModel() {
        PNRModel pnrModel = new PNRModel();
        BeanUtils.copyProperties(pnrModel, this);
        return pnrModel;
    }

    @Override
    public int compareTo(Object o) {
       return this.getMessageseq().compareTo(((PNREntity)o).getMessageseq());
    }
}
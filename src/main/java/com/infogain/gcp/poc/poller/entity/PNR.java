package com.infogain.gcp.poc.poller.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Column;
import org.springframework.cloud.gcp.data.spanner.core.mapping.PrimaryKey;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Table;

import com.google.cloud.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"pnr"})
@Table(name = "pnr")
public class PNR {

	@PrimaryKey
	@Column(name = "pnr")
	private String pnr;

	private String mobileNumber;

	private String remark;

	@Column(name = "lastUpdateTimestamp")
	private Timestamp lastUpdateTimestamp;

}
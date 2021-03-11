package com.infogain.gcp.poc.poller.entity;

import org.springframework.cloud.gcp.data.spanner.core.mapping.Column;
import org.springframework.cloud.gcp.data.spanner.core.mapping.PrimaryKey;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Table;

import com.google.cloud.Timestamp;

@Table(name = "pnr")
public class PNR {
	@PrimaryKey
	@Column(name = "pnr")
	private String pnr;
	private String mobileNumber;
	private String remark;
	@Column(name = "lastUpdateTimestamp")
	private Timestamp lastUpdateTimestamp;

	public String getPnr() {
		return pnr;
	}

	public void setPnr(String pnr) {
		this.pnr = pnr;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Timestamp getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Timestamp lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	@Override
	public String toString() {
		return "PNR [pnr=" + pnr + ", mobileNumber=" + mobileNumber + ", remark=" + remark + ", lastUpdateTimestamp="
				+ lastUpdateTimestamp + "]";
	}

}

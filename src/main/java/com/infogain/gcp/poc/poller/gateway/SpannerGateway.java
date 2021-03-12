package com.infogain.gcp.poc.poller.gateway;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.data.spanner.core.SpannerTemplate;
import org.springframework.stereotype.Component;

import com.google.cloud.Timestamp;
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.Statement;

@Component
public class SpannerGateway {

	private SpannerTemplate spannerTemplate;

	@Autowired
	public SpannerGateway(SpannerTemplate spannerTemplate) {
		this.spannerTemplate = spannerTemplate;
	}

	public Timestamp getTimestampRecord(Statement statement) {
		Timestamp timestamp = null;
		ResultSet rs = spannerTemplate.executeQuery(statement, null);
		if (rs.next()) {
			timestamp = rs.isNull(0) ? timestamp : rs.getTimestamp(0);
		}

		return timestamp;
	}

	public <T> List<T> getAllRecord(Statement statement, Class<T> type) {
		return this.spannerTemplate.query(type, statement, null);
	}


public void save(Statement statement) {
	this.spannerTemplate.executeDmlStatement(statement);
}
}

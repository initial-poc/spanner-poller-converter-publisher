package com.infogain.gcp.poc.constant;

public interface ApplicationConstant {

	String CURRENT_TIMESTAMP_QUERY = "SELECT CURRENT_TIMESTAMP";
	String LAST_COMIMT_TIMESTAMP_QUERY = "SELECT max(last_commit_timestamp) FROM POLLER_COMMIT_TIMESTAMPS";
	
	String PREVIOUS_TIMESTAMP_PLACE_HOLDER="prevCommitTimestamp";
	String LAST_POLLER_EXECUTION_TIMESTAMP_PLACE_HOLDER="lastPollerExecutionCommitTimestamp";
	String POLL_QUERY = "SELECT * FROM pnr WHERE lastUpdateTimestamp>@"+PREVIOUS_TIMESTAMP_PLACE_HOLDER+ " order by lastUpdateTimestamp";
	String LAST_COMIMT_TIMESTAMP_SAVE_QUERY = "INSERT INTO POLLER_COMMIT_TIMESTAMPS (last_commit_timestamp) VALUES(@"+LAST_POLLER_EXECUTION_TIMESTAMP_PLACE_HOLDER+")";

}

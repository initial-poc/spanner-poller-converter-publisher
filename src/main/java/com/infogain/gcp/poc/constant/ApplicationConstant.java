
package com.infogain.gcp.poc.constant;

public class ApplicationConstant {
	private ApplicationConstant() {
	}

	public static final String CURRENT_TIMESTAMP_QUERY = "SELECT CURRENT_TIMESTAMP";
	public static final String LAST_COMIMT_TIMESTAMP_QUERY = "SELECT max(last_commit_timestamp) FROM POLLER_COMMIT_TIMESTAMPS";

	public static final String PREVIOUS_TIMESTAMP_PLACE_HOLDER = "prevCommitTimestamp";
	public static final String LAST_POLLER_EXECUTION_TIMESTAMP_PLACE_HOLDER = "lastPollerExecutionCommitTimestamp";
	public static final String POLL_QUERY = "SELECT * FROM pnr WHERE lastUpdateTimestamp>@"
			+ PREVIOUS_TIMESTAMP_PLACE_HOLDER + " order by lastUpdateTimestamp";
	public static final String LAST_COMIMT_TIMESTAMP_SAVE_QUERY = "INSERT INTO POLLER_COMMIT_TIMESTAMPS (last_commit_timestamp) VALUES(@"
			+ LAST_POLLER_EXECUTION_TIMESTAMP_PLACE_HOLDER + ")";

}

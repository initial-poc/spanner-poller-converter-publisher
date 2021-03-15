# Google cloud spanner poller converter and pub sub publisher

* Create maven project using below command
```
mvn archetype:generate -DgroupId=com.infogain.gcp.poc -DartifactId=spanner-poller-converter-publisher -Dversion=1.0.0 -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

# Spanner DDL scripts
* pnr table
```
CREATE TABLE pnr (
    pnr STRING(MAX),
    lastUpdateTimestamp TIMESTAMP NOT NULL OPTIONS (allow_commit_timestamp=true),
    mobileNumber STRING(MAX),
    remark STRING(MAX),
) PRIMARY KEY (pnr);
```
* poller commit timestamp
```
CREATE TABLE POLLER_COMMIT_TIMESTAMPS (
    last_commit_timestamp TIMESTAMP,
) PRIMARY KEY (last_commit_timestamp);
```
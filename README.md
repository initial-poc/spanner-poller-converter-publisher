# Google cloud spanner poller converter and pub sub publisher

* Project clone
```

```

# Spanner DDL scripts
* group_message_store table
```
CREATE TABLE group_message_store (
	pnrid STRING(MAX) NOT NULL,
	messageseq STRING(MAX) NOT NULL,
	status STRING(MAX),
	payload STRING(MAX),
	timestamp TIMESTAMP,
) PRIMARY KEY (pnrid, messageseq, status)
```
 
```
 
 
 CREATE UNIQUE INDEX pnr_index 
ON group_message_store (
	pnrid,
	messageseq,
	status
)
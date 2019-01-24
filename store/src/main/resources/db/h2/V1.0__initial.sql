CREATE TABLE "alerts" (
    "id"                VARCHAR(36)     NOT NULL PRIMARY KEY,
    "timestamp"         BIGINT          NOT NULL,
    "application"       VARCHAR(64),
    "userMessage"       VARCHAR(1024),
    "technicalMessage"  VARCHAR(1024),
    "rightMask"         BIGINT,
    "severity"          INTEGER         NOT NULL,
    "category"          INTEGER         NOT NULL,
    "subCategory"       INTEGER,
    "origin"            INTEGER,
    "status"            INTEGER         NOT NULL,
    "version"           INTEGER,
    "asset"             VARCHAR(128),
    "realm"             VARCHAR(128)
);


CREATE TABLE "alert_tags" (
  "id"                VARCHAR(36)     NOT NULL,
  "tag"               VARCHAR(128)    NOT NULL,
  CONSTRAINT alert_tags_id_in_alert_id FOREIGN KEY ("id") REFERENCES "alerts" ("id")
);
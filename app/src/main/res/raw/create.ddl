DROP TABLE IF EXISTS survey_detail;
DROP TABLE IF EXISTS container_location;
DROP TABLE IF EXISTS container_type;
DROP TABLE IF EXISTS survey;
DROP TABLE IF EXISTS user_profile;
DROP TABLE IF EXISTS organization;
DROP TABLE IF EXISTS building;
DROP TABLE IF EXISTS place;
DROP TABLE IF EXISTS place_subtype;
DROP TABLE IF EXISTS place_type;
DROP TABLE IF EXISTS subdistrict;
DROP TABLE IF EXISTS district;
DROP TABLE IF EXISTS province;

CREATE TABLE building (
  building_id VARCHAR(36)  NOT NULL,
  name        VARCHAR(254) NOT NULL,
  place_id    VARCHAR(36)  NOT NULL,
  latitude    NUMERIC(19, 16),
  longitude   NUMERIC(19, 16),
  update_time timestamp    NOT NULL,
  changed_status INTEGER(10) DEFAULT 0 NOT NULL,
  update_by   VARCHAR(128) NOT NULL,
  PRIMARY KEY (building_id),
  FOREIGN KEY (place_id) REFERENCES place (place_id) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE container_location (
  container_location_id INTEGER     NOT NULL PRIMARY KEY,
  name                  VARCHAR(80) NOT NULL
);
CREATE TABLE container_type (
  container_type_id INTEGER     NOT NULL PRIMARY KEY,
  name              VARCHAR(80) NOT NULL
);
CREATE TABLE district (
  district_code VARCHAR(4)  NOT NULL,
  name          VARCHAR(80) NOT NULL,
  province_code VARCHAR(2)  NOT NULL,
  boundary      TEXT,
  PRIMARY KEY (district_code),
  FOREIGN KEY (province_code) REFERENCES province (province_code)
);
CREATE TABLE organization (
  org_id             INTEGER      NOT NULL PRIMARY KEY,
  name               VARCHAR(128) NOT NULL,
  address            TEXT,
  subdistrict_code   VARCHAR(6)   NOT NULL,
  health_region_code VARCHAR(6)   NOT NULL
);
CREATE TABLE place (
  place_id         VARCHAR(36)  NOT NULL,
  name             VARCHAR(254) NOT NULL,
  subtype_id       SMALLINT(5)  NOT NULL,
  subdistrict_code VARCHAR(6)   NOT NULL,
  latitude         NUMERIC(19, 16),
  longitude        NUMERIC(19, 16),
  update_time      timestamp    NOT NULL,
  changed_status   INTEGER(10) DEFAULT 0 NOT NULL,
  update_by        VARCHAR(128) NOT NULL,
  PRIMARY KEY (place_id),
  FOREIGN KEY (subtype_id) REFERENCES place_subtype (subtype_id) ON UPDATE CASCADE,
  FOREIGN KEY (subdistrict_code) REFERENCES subdistrict (subdistrict_code) ON UPDATE CASCADE
);
CREATE TABLE place_subtype (
  subtype_id INTEGER      NOT NULL PRIMARY KEY,
  name       VARCHAR(100) NOT NULL,
  type_id    SMALLINT(5)  NOT NULL,
  FOREIGN KEY (type_id) REFERENCES place_type (type_id) ON UPDATE CASCADE
);
CREATE TABLE place_type (
  type_id INTEGER     NOT NULL PRIMARY KEY,
  name    VARCHAR(80) NOT NULL
);
CREATE TABLE province (
  province_code VARCHAR(2)  NOT NULL,
  name          VARCHAR(80) NOT NULL,
  boundary      TEXT,
  PRIMARY KEY (province_code)
);
CREATE TABLE subdistrict (
  subdistrict_code VARCHAR(6)  NOT NULL,
  name             VARCHAR(80) NOT NULL,
  district_code    VARCHAR(4)  NOT NULL,
  boundary         TEXT,
  PRIMARY KEY (subdistrict_code),
  FOREIGN KEY (district_code) REFERENCES district (district_code)
);
CREATE TABLE survey (
  survey_id    VARCHAR(36)  NOT NULL,
  building_id  VARCHAR(36)  NOT NULL,
  person_count INTEGER(10)  NOT NULL,
  surveyor     VARCHAR(128) NOT NULL,
  latitude     INTEGER(10),
  longitude    INTEGER(10),
  create_time  timestamp    NOT NULL,
  update_time  timestamp    NOT NULL,
  changed_status  INTEGER(10) DEFAULT 0 NOT NULL,
  remark       VARCHAR(254),
  PRIMARY KEY (survey_id),
  FOREIGN KEY (building_id) REFERENCES building (building_id) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (surveyor) REFERENCES user_profile (username) ON UPDATE CASCADE
);
CREATE TABLE survey_detail (
  detail_id             VARCHAR(36) NOT NULL,
  survey_id             VARCHAR(36) NOT NULL,
  container_type_id     INTEGER(10) NOT NULL,
  container_location_id INTEGER(10) NOT NULL,
  container_count       INTEGER(10) NOT NULL,
  container_have_larva  INTEGER(10) NOT NULL,
  PRIMARY KEY (detail_id),
  CONSTRAINT survey_containerLocation_containerType_unique
  UNIQUE (survey_id, container_type_id, container_location_id),
  FOREIGN KEY (container_location_id) REFERENCES container_location (container_location_id) ON UPDATE CASCADE,
  FOREIGN KEY (container_type_id) REFERENCES container_type (container_type_id) ON UPDATE CASCADE,
  FOREIGN KEY (survey_id) REFERENCES survey (survey_id) ON UPDATE CASCADE ON DELETE CASCADE
);
CREATE TABLE user_profile (
  username        VARCHAR(128) NOT NULL,
  org_id          INTEGER(10)  NOT NULL,
  password        VARCHAR(32),
  firstname       VARCHAR(64),
  lastname        VARCHAR(64),
  avatar_filename VARCHAR(128),
  email           VARCHAR(64),
  phone_number    VARCHAR(64),
  PRIMARY KEY (username),
  FOREIGN KEY (org_id) REFERENCES organization (org_id) ON UPDATE CASCADE
);
CREATE INDEX building_place_id
ON building (place_id);
CREATE INDEX district_province_code
ON district (province_code);
CREATE INDEX place_subdistrict_code
ON place (subdistrict_code);
CREATE INDEX subdistrict_district_code
ON subdistrict (district_code);
CREATE INDEX survey_building_id
ON survey (building_id);
CREATE INDEX survey_surveyor
ON survey (surveyor);
CREATE INDEX survey_detail_survey_id
ON survey_detail (survey_id);

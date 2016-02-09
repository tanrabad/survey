INSERT INTO
  organization (org_id, name, subdistrict_code, health_region_code)
VALUES
  ("100", "กรมควบคุมโรค", "120105", "1");

INSERT INTO
  user_profile (username, org_id)
VALUES
  ("dpc-user", "100");
  ("dpc-13-beta", "100");
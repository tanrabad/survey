INSERT INTO
  organization (org_id, name, subdistrict_code, health_region_code)
VALUES
  ("13", "สำนักงานป้องกันควบคุมโรคที่ 13", "100502", "4");

INSERT INTO
  user_profile (username, org_id)
VALUES
  ("dpc-user", "13"),
  ("dpc-13-beta", "13"),
  ("trial-debug", "13"),
  ("trial-beta", "13"),
  ("trial-release", "13");

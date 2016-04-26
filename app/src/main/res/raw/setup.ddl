INSERT INTO
  organization (org_id, name, subdistrict_code, health_region_code)
VALUES
  ("99999", "สำนักงานป้องกันควบคุมโรคที่ 99999", "100502", "dpc-13");

INSERT INTO
  user_profile (username, org_id, firstname, lastname, api_filter)
VALUES
  ("trial-debug", "99999", "ทดสอบ", "ดีบัก", "hr_code=dpc-13"),
  ("trial-beta", "99999", "ทดสอบ", "เบต้า", "hr_code=dpc-13"),
  ("trial-release", "99999", "ทดสอบ", "ของจริง", "hr_code=dpc-13");

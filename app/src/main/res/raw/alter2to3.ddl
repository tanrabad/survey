ALTER TABLE user_profile ADD COLUMN api_filter VARCHAR(128);

INSERT INTO
  organization (org_id, name, subdistrict_code, health_region_code)
VALUES
  ("99999", "สำนักงานป้องกันควบคุมโรคที่ 99999", "100502", "dpc-13");

UPDATE user_profile
SET api_filter = "hr_code=dpc-13", org_id=99999, firstname="ทดสอบ", lastname="ดีบัก"
WHERE username = 'trial-debug';
UPDATE user_profile
SET api_filter = "hr_code=dpc-13", org_id=99999, firstname="ทดสอบ", lastname="เบต้า"
WHERE username = 'trial-beta';
UPDATE user_profile
SET api_filter = "hr_code=dpc-13", org_id=99999, firstname="ทดสอบ", lastname="ของจริง"
WHERE username = 'trial-release';

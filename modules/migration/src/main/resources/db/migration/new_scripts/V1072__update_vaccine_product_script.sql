--Delete from vaccine_product_targets where productid in (2417,2429);
--INSERT INTO vaccine_product_targets (productid,targetwastagegood, targetwastagewarn, targetwastagebad, targetcoveragebad, targetcoveragewarn, targetcoveragegood, targetdropoutgood, targetdropoutwarn, targetdropoutbad, targetwastageclosedvialsgood, targetwastageclosedvialswarn, targetwastageclosedvialsbad) VALUES ('2417', '5', '5', '5', '50', '80', '90', '5', '10', '20', '5', '10', '20');
--INSERT INTO vaccine_product_targets (productid,targetwastagegood, targetwastagewarn, targetwastagebad, targetcoveragebad, targetcoveragewarn, targetcoveragegood, targetdropoutgood, targetdropoutwarn, targetdropoutbad, targetwastageclosedvialsgood, targetwastageclosedvialswarn, targetwastageclosedvialsbad) VALUES ('2429', '5', '5', '5', '50', '80', '90', '5', '10', '20', '5', '10', '20');

-- View: vw_vaccine_coverage


ALTER TABLE VACCINE_PRODUCT_DOSES
    DROP COLUMN IF EXISTS dashboardDisplayOrder;

ALTER TABLE VACCINE_PRODUCT_DOSES
    ADD COLUMN dashboardDisplayOrder INTEGER;

ALTER TABLE VACCINE_PRODUCT_DOSES
    DROP COLUMN IF EXISTS displayindashboard;

ALTER TABLE VACCINE_PRODUCT_DOSES
    ADD COLUMN displayindashboard BOOLEAN DEFAULT FALSE;


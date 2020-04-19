DO $$
    BEGIN
        BEGIN
            ALTER TABLE programs ADD COLUMN isMonthlyCovid Boolean default false;
        EXCEPTION
            WHEN duplicate_column THEN RAISE NOTICE 'column isMonthlyCovid already exists in programs.';
        END;
    END;
$$;

DELETE FROM PROGRAMS WHERE CODE = 'MONTHLY_COVID_19';

INSERT INTO programs (code, name, description, active, templateConfigured, regimenTemplateConfigured, budgetingApplies, usesDar, push,CANTRACKCOVID, isMonthlyCovid)
VALUES

('MONTHLY_COVID_19', 'MONTHLY COVID-19', 'MONTHLY COVID-19', TRUE, FALSE, FALSE, FALSE, FALSE, FALSE, false, true);

DELETE FROM rights where name ='VIEW_NATIONAL_LEVEL_REPORTS';
INSERT INTO rights (
    NAME,
    righttype,
    description,
    createddate,
    displayorder,
    displaynamekey
)
VALUES
(
    'VIEW_NATIONAL_LEVEL_REPORTS',
    'REPORT',
    'Permission to view national level reports',
    now(),
    NULL,
    'right.report.national.level.report'
);

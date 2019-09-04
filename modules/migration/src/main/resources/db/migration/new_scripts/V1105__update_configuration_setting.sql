delete from configuration_settings where key = 'DASHLET_STOCK_AVAILABILITY_BY_PROGRAM_DRILL_DOWN';
INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('DASHLET_STOCK_AVAILABILITY_BY_PROGRAM_DRILL_DOWN',
  'Dashlet Stock Availability for program drill down','Dashboard','',' N/A','TEXT_AREA', 42);




  delete from configuration_settings where key = 'DASHLET_STOCK_AVAILABILITY_BY_LEVEL';
  INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
    values ('DASHLET_STOCK_AVAILABILITY_BY_LEVEL',
    'Dashlet Stock Availability by level','Dashboard','','

  Definition
  Percentage of health facilities that have not experienced stock out incidences of Health Commodities in the specified reporting period.

  Purpose
  This indicator helps to measure stock availability during a specified period according to the Essential list of Health Commodities at each level.
  This measure shows the health system(s) ability to meet the needs of customers.

  calculation
  ( Number of Health commodities stock out incidents
  /
  Total number of possible incidents of health commodities stock outs across HFs ) * 100

    ','TEXT_AREA', 42);



delete from configuration_settings where key = 'DASHLET_REPORTING_TIMELINESS';
INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('DASHLET_REPORTING_TIMELINESS',
  'Dashlet Reporting Timeliness','Dashboard','','Definition To be added','TEXT_AREA', 42);



 delete from configuration_settings where key = 'DASHLET_STOCK_STATUS_OVER_TIME';
INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('DASHLET_STOCK_STATUS_OVER_TIME',
  'Dashlet Stock Status Over Time','Dashboard','',' Definition to be Added','TEXT_AREA', 42);

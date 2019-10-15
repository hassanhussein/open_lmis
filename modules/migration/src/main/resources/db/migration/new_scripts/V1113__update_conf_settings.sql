delete from configuration_settings where key = 'DASHLET_STOCK_AVAILABILITY';
INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('DASHLET_STOCK_AVAILABILITY',
  'Dashlet Stock Availability Summary','Dashboard','','Definition To be added','TEXT_AREA', 42);




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




--Dashlet 1

delete from configuration_settings where key = 'DASHLET_STOCK_AVAILABILITY';
INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('DASHLET_STOCK_AVAILABILITY',
  'Dashlet Stock Availability','Dashboard','','

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


--2

delete from configuration_settings where key = 'DASHLET_STOCK_AVAILABILITY_BY_LEVEL';
INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('DASHLET_STOCK_AVAILABILITY_BY_LEVEL',
  'Dashlet Stock Availability','Dashboard','','

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



--3



delete from configuration_settings where key = 'DASHLET_STOCK_AVAILABILITY_BY_PROGRAM';
INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('DASHLET_STOCK_AVAILABILITY_BY_PROGRAM',
  'Dashlet Stock Availability for program','Dashboard','','

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



--Another definition

delete from configuration_settings where key = 'DASHLET_ON_TIME_DELIVERY';
INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('DASHLET_ON_TIME_DELIVERY',
  'On-Time Delivery of Health Facilities','Dashboard','','

Definition
Percentage of health facilities that delivered on Time.

Purpose
This indicator helps to measure stock availability during a specified period according to the Essential list of Health Commodities at each level.
This measure shows the health system(s) ability to meet the needs of customers.

calculation
( Number of Health commodities stock out incidents
/
Total number of possible incidents of health commodities stock outs across HFs ) * 100

  ','TEXT_AREA', 42);



--wastage


delete from configuration_settings where key = 'DASHLET_WASTAGE';
INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('DASHLET_WASTAGE',
  'Percentage Wastage','Dashboard','','

Definition
Percentage of health facilities that delivered on Time.

Purpose
This indicator helps to measure stock availability during a specified period according to the Essential list of Health Commodities at each level.
This measure shows the health system(s) ability to meet the needs of customers.

calculation
( Number of Health commodities stock out incidents
/
Total number of possible incidents of health commodities stock outs across HFs ) * 100

  ','TEXT_AREA', 42);



--quality check

delete from configuration_settings where key = 'RNR_QUALITY_CHECK';
INSERT INTO configuration_settings (key, name, groupname, description, value, valueType,displayOrder)
  values ('RNR_QUALITY_CHECK',
  'Percentage of R&R passed data quality check','Dashboard','','

Definition
Percentage of health facilities that delivered on Time.

Purpose
This indicator helps to measure stock availability during a specified period according to the Essential list of Health Commodities at each level.
This measure shows the health system(s) ability to meet the needs of customers.

calculation
( Number of Health commodities stock out incidents
/
Total number of possible incidents of health commodities stock outs across HFs ) * 100

  ','TEXT_AREA', 42);














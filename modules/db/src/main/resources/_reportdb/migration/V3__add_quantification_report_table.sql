
DROP TABLE IF EXISTS quantification_excercise_reporting_data;
CREATE TABLE public.quantification_excercise_reporting_data
(
  id serial,
  rnrId integer,
  programId integer,
  periodId integer,
  scheduleId integer,
  district_id integer,
  region_id integer,
  zone_id integer,
  facilityid integer,
  zone_name character varying(250),
  region_name character varying(250),
  district_name character varying(250),
  programName character varying(250),
  facility character varying(250),
  facilityCode character varying(250),
  facilityType character varying(250),
  code character varying(250),
  product character varying(250),
  category character varying(250),
  uom integer,
  issues integer,
  createddate timestamp without time zone DEFAULT now(),
  modifieddate timestamp without time zone DEFAULT now(),

  CONSTRAINT quantification_excercise_reporting_data_pk PRIMARY KEY (id)

)
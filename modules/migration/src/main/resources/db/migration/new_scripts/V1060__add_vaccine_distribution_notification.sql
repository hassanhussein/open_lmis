 DROP TABLE IF EXISTS public.distribution_notifications;

CREATE TABLE public.distribution_notifications
(
  id serial,
  distributionId INTEGER,
  fromFacility character varying(250) NOT NULL,
  toFacility character varying(250) NOT NULL,
  geographiczoneid integer,
  facilityId INTEGER,
  allowedToSend boolean NOT NULL DEFAULT false,
  sent boolean NOT NULL DEFAULT false,
  createddate timestamp without time zone DEFAULT now(),
  CREATEDBY INTEGER,
  CONSTRAINT distribution_notifications_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.distribution_notifications
  OWNER TO postgres;
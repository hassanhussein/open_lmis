-- View: public.mv_facilitiy_consumption
 DROP MATERIALIZED VIEW if EXISTS public.mv_facilitiy_consumption;

CREATE MATERIALIZED VIEW public.mv_facilitiy_consumption
TABLESPACE pg_default
AS
 SELECT p.code,
    pp.name AS periodname,
    pp.startdate AS periodstart,
    f.id AS facilityid,
    f.name AS facility,
    f.code AS facilitycode,
    ft.name AS facilitytype,
    (f.code::text || '_'::text) || p.code::text AS facprodcode,
    ((((((p.primaryname::text || ' '::text) || COALESCE(p.strength, ''::character varying)::text) || ' '::text) || COALESCE(ds.code, ''::character varying)::text) || ' ('::text) || COALESCE(p.dispensingunit, '-'::character varying)::text) || ')'::text AS product,
    sum(li.quantitydispensed) AS dispensed,
    sum(li.normalizedconsumption) AS consumption,
    ceil(sum(li.quantitydispensed)::double precision / (sum(li.packsize) / count(li.productcode))::double precision) AS consumptioninpacks,
    ceil(sum(li.normalizedconsumption)::double precision / (sum(li.packsize) / count(li.productcode))::double precision) AS adjustedconsumptioninpacks
   FROM requisition_line_items li
     JOIN requisitions r ON r.id = li.rnrid
     JOIN facilities f ON r.facilityid = f.id
     JOIN vw_districts d ON d.district_id = f.geographiczoneid
     JOIN processing_periods pp ON pp.id = r.periodid
     JOIN products p ON p.code::text = li.productcode::text
     JOIN program_products ppg ON ppg.programid = r.programid AND ppg.productid = p.id
     JOIN facility_types ft ON ft.id = f.typeid
     JOIN dosage_units ds ON ds.id = p.dosageunitid
  GROUP BY f.id, f.name, ft.name, p.code, p.primaryname, p.dispensingunit, p.strength, ds.code, pp.name, pp.startdate
WITH NO DATA;

ALTER TABLE public.mv_facilitiy_consumption
    OWNER TO postgres;
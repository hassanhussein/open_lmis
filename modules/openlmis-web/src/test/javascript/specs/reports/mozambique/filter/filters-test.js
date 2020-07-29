select
 districtId,district_name districtName,
 region_name as regionName,zone_name as zoneName, count(*) rejectedCount 
 from ( select count(*), rnrid, zone_name,d.district_id districtId,d.district_name,d.region_name 
         from requisition_status_changes c  
         join requisitions r on r.id = c.rnrid  
         join facilities f on f.id = r.facilityid  
         join vw_districts d on d.district_id = f.geographiczoneid  
         where c.status = '  filter.getStatus()  '
		 and r.programid =  filter.getProgram()   
		 and r.periodId =   filter.getPeriod()   
		 group by rnrid, d.zone_name,d.district_id ,d.district_name,d.region_name 
		 having count(*) > 1  ) a 
                                group by districtId, a.district_name,a.region_name, a.zone_name 
                                
                                order by a.region_name, a.district_name
package org.openlmis.analytics.Repository;

import org.openlmis.analytics.Repository.Mapper.AnalyticsMapper;
import org.openlmis.analytics.Repository.Mapper.GeoCoordinateGeneratorMapper;
import org.openlmis.analytics.domain.Dashboard;
import org.openlmis.core.domain.GeographicZone;
import org.openlmis.core.dto.GeographicZoneGeometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class DashboardRepository {

    @Autowired
    private AnalyticsMapper mapper;

    @Autowired
    private GeoCoordinateGeneratorMapper geoCoordinateGeneratorMapper;

    public List<Dashboard> getUsers(){
        return mapper.getUsers();
    }

    public List<HashMap<String,Object>> getStockStatusSummary(Long userId, Long product, Long program, Long year,Long schedule){
        return mapper.getStockStatusSummary(userId,product,program,year,schedule);
    }

    public List<HashMap<String,Object>> getStockAvailableForPeriod(Long userId, Long period){
        return mapper.getStockAvailableForPeriod(userId,period);
    }

    public List<HashMap<String,Object>> getStockForProductandProgram(Long userId,Long program, Long period){
        return mapper.getStockForProductandProgram(userId,program,period);
    }

    public List<HashMap<String,Object>> getConsumptioTrends(Long userId, Long year){
        return mapper.getConsumptioTrends(userId,year);
    }

    public List<GeographicZone> getAllProvinces () {
        return geoCoordinateGeneratorMapper.getAllProvinces();
    }
    public List<GeographicZone> getDistrictByParentId (Long parent) {
        return geoCoordinateGeneratorMapper.getDistrictByParentId(parent);
    }
    public GeographicZoneGeometry getGeometryById (Long parent) {
        return geoCoordinateGeneratorMapper.getGeometryById(parent);
    }
}
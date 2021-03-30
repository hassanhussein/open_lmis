package org.openlmis.analytics.service;

import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openlmis.analytics.Repository.DashboardRepository;
import org.openlmis.analytics.domain.Dashboard;
import org.openlmis.analytics.dto.Features;
import org.openlmis.analytics.dto.Geometry;
import org.openlmis.analytics.dto.Properties;
import org.openlmis.analytics.dto.Type;
import org.openlmis.core.domain.GeographicZone;
import org.openlmis.core.dto.GeographicZoneGeometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Component
@NoArgsConstructor
public class DashboardService{

    @Autowired
    private DashboardRepository repository;

    public List<Dashboard> getAllUsers() {
        return repository.getUsers();
    }

    public List<HashMap<String,Object>> getStockStatusSummary(Long userId, Long product, Long program, Long year,Long schedule){
        return repository.getStockStatusSummary(userId,product,program,year,schedule);
    }

    public List<HashMap<String,Object>> getStockAvailableForPeriod(Long userId, Long period){
        return repository.getStockAvailableForPeriod(userId,period);
    }

    public List<HashMap<String,Object>> getStockForProductandProgram(Long userId,Long program, Long period){
        return repository.getStockForProductandProgram(userId,program,period);
    }

    public List<HashMap<String,Object>> getConsumptioTrends(Long userId, Long year){
        return repository.getConsumptioTrends(userId,year);
    }

    public List createMapObject() {
      List<GeographicZone> geographicZones = repository.getAllProvinces();
        int a = 5;

        List base = new LinkedList();

      for(GeographicZone geoZone : geographicZones) {

          List<GeographicZone> gz = repository.getDistrictByParentId(geoZone.getId());
          if(!gz.isEmpty()) {

              for(GeographicZone gg: gz) {
                 Features  features = new Features();
                  GeographicZoneGeometry geometry1 = repository.getGeometryById(gg.getId());
                  if(geometry1 != null) {
                      Properties properties = new Properties();
                      properties.setLBL("ZM-" + a++);
                      properties.setFIP("ZM");
                      properties.setMMT_ID("ZAM");
                      properties.setSHORT__FRM("Zambia");
                      properties.setLONG_FRM("Zambia");
                      properties.setADM0("Zambia");
                      properties.setADM1(geoZone.getName());
                      properties.setADM2(gg.getName());
                      properties.setADM3("-");
                      properties.setADM4("-");
                      properties.setADM5("-");
                      properties.setSTL0(260);
                      properties.setSTL1(1);
                      properties.setSTL2(1);
                      properties.setSTL3("-");
                      properties.setSTL4("-");
                      properties.setSTL5("-");
                      properties.setHcKey(gg.getName());
                      properties.setName(gg.getName());

                      Geometry geometry = new Geometry();
                      geometry.setGeometry(geometry1.getGeometry());
                      features.setType("Feature");
                      features.setProperties(properties);
                      features.setGeometry(geometry1.getGeometry());

                      base.add(new JSONObject(features).toMap());
                  }

              }


          }

      }
      return base;
    }

}


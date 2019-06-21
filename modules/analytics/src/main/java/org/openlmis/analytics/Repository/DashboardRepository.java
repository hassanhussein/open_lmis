package org.openlmis.analytics.Repository;

import org.openlmis.analytics.Repository.Mapper.AnalyticsMapper;
import org.openlmis.analytics.domain.Dashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class DashboardRepository {

    @Autowired
    private AnalyticsMapper mapper;

    public List<Dashboard> getUsers(){
        return mapper.getUsers();
    }

    public List<HashMap<String,Object>> getStockStatusSummary(Long userId, Long product, Long program, Long year){
        return mapper.getStockStatusSummary(userId,product,program,year);
    }

    public List<HashMap<String,Object>> getStockAvailableForPeriod(Long userId, Long period){
        return mapper.getStockAvailableForPeriod(userId,period);
    }
}
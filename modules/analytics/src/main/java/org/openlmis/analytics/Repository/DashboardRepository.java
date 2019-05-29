package org.openlmis.analytics.Repository;

import org.openlmis.analytics.Repository.Mapper.AnalyticsMapper;
import org.openlmis.analytics.domain.Dashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DashboardRepository {

    @Autowired
    private AnalyticsMapper mapper;

    public List<Dashboard> getUsers(){
        return mapper.getUsers();
    }
}
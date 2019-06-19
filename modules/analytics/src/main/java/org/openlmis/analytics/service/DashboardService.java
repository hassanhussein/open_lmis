package org.openlmis.analytics.service;

import lombok.NoArgsConstructor;
import org.openlmis.analytics.Repository.DashboardRepository;
import org.openlmis.analytics.domain.Dashboard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@NoArgsConstructor
public class DashboardService{

    @Autowired
    private DashboardRepository repository;

    public List<Dashboard> getAllUsers() {
        return repository.getUsers();
    }

    public List<HashMap<String,Object>> getStockStatusSummary(Long userId, Long product, Long program, Long year){
        return repository.getStockStatusSummary(userId,product,program,year);
    }

}


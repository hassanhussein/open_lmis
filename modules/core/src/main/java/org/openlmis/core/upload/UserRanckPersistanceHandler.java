package org.openlmis.core.upload;

import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.GeographicZone;
import org.openlmis.core.dto.UserRank;
import org.openlmis.core.service.GeographicZoneService;
import org.openlmis.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRanckPersistanceHandler extends AbstractModelPersistenceHandler {
    UserService userService;


    @Autowired
    public UserRanckPersistanceHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected BaseModel getExisting(BaseModel record) {
        return userService.getUserRankBy((UserRank) record);
    }

    @Override
    protected void save(BaseModel record) {
        userService.saveUserRank((UserRank)record);
    }
}

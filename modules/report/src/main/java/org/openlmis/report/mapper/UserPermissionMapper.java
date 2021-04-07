package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.openlmis.core.domain.User;
import org.openlmis.report.model.dto.Facility;
import org.openlmis.report.model.dto.StockStatusDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPermissionMapper {

    @Select(" SELECT * FROM  users U  " +
            " INNER JOIN role_assignments RA ON RA.userId = U.id INNER JOIN role_rights RR ON RR.roleId = RA.roleId " +
            "   WHERE U.id = #{userId} AND RR.rightName IN ('VIEW_NATIONAL_LEVEL_REPORTS') ")
    List<User> getPermissionToViewNationalReport(@Param("userId") Long userId);

}

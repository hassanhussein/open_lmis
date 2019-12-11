package org.openlmis.core.audit;

import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditMapper {

    @Insert("insert into audit_trails (action, userfullname, userid, identity, " +
            "identityvalue, oldvalue, newvalue, createddate) " +
            " VALUES (#{actionFullName}, (select concat(firstname, ' ', lastname) from users where id = #{userId} limit 1), " +
            "#{userId}, #{identityKey}, #{identityValue}, #{oldValue}, #{newValue}, now())")
    void logActivity(Audit audit);
}
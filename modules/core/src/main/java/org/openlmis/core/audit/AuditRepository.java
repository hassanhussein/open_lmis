package org.openlmis.core.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AuditRepository {

    @Autowired
    private AuditMapper mapper;


    public void logActivity(Audit audit) {
        mapper.logActivity(audit);
    }
}
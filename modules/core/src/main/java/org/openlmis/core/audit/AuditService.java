package org.openlmis.core.audit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.openlmis.core.domain.BaseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class AuditService {

    @Autowired
    AuditRepository auditRepository;

    Javers javers;

    public void logActivity(Audit audit){
        auditRepository.logActivity(audit);
    }

    public void logActivity(BaseModel object, AuditAction action) {

       // String code = getFieldValue(object, "Code");
        //String userName = getFieldValue(object, "UserName");
        Long id = object.getId();
        Long userId = object.getModifiedBy() != null ? object.getModifiedBy() : object.getCreatedBy();

        Audit audit;

      //  if(code != null)
       //     audit = new Audit(object.getClass(), action, AuditIdentityKey.CODE, code,
       //             null,null, userId, null);
       // if(userName != null)
      //      audit = new Audit(object.getClass(), action, AuditIdentityKey.USERNAME, userName,
       //             null,null, userId, null);
        if(id != null) {
            audit = new Audit(object.getClass(), action, AuditIdentityKey.ID, id.toString(), null,
                    null, userId,null);
        }
        else
            return;

        auditRepository.logActivity(audit);
    }

    public void logActivity(BaseModel newObject, BaseModel oldObject) {

        List<String> auditFields = Arrays.asList("code", "name", "dispensingUnit", "dosesPerDispensingUnit",
                "mslPackSize", "packSize", "packRoundingThreshold", "facilityType.name", "dosageUnit.code", "primaryName");

        javers = javers == null ?  JaversBuilder.javers().build() : javers;

        Long userId = newObject.getCreatedBy() != null ? newObject.getCreatedBy() : newObject.getModifiedBy();

        Diff diff = javers.compare(oldObject, newObject);

        diff.getChangesByType(ValueChange.class).forEach(change -> {
            if(auditFields.contains(change.getPropertyNameWithPath())) {
                Audit audit = new Audit(newObject.getClass(),
                        AuditAction.valueOf("CHANGE_" + change.getPropertyNameWithPath()
                                .replace(".", "_")
                                .toUpperCase()
                        ),
                        AuditIdentityKey.ID, //indentity
                        newObject.getId().toString(), //Indentity value
                        Optional.ofNullable(change.getLeft()).isPresent() ? change.getLeft().toString() : null, //old value
                        Optional.ofNullable(change.getRight()).isPresent() ? change.getRight().toString() : null,//new value
                        userId, null);
                auditRepository.logActivity(audit);
            }
        });
    }

    private String getFieldValue(Object object, String columnName) {
        try {
            Method method = object.getClass().getDeclaredMethod("get"+columnName, null);
            Object returnValue = method.invoke(object);
            return returnValue == null ? null : returnValue.toString();
        } catch (NoSuchMethodException e) {
           return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }
}

package org.openlmis.core.audit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class AuditService {

    @Autowired
    AuditRepository auditRepository;

    public void logActivity(Audit audit){
        auditRepository.logActivity(audit);
    }

    public void logActivity(BaseModel object, AuditAction action) {

        String code = getFieldValue(object, "Code");
        String userName = getFieldValue(object, "UserName");
        Long id = object.getId();
        Long userId = object.getModifiedBy() != null ? object.getModifiedBy() : object.getCreatedBy();

        Audit audit;

        if(code != null)
            audit = new Audit(object.getClass(), action, AuditIdentityKey.CODE, code,
                    null,null, userId, null);
        else if(userName != null)
            audit = new Audit(object.getClass(), action, AuditIdentityKey.USERNAME, userName,
                    null,null, userId, null);
        else if(id != null) {
            audit = new Audit(object.getClass(), action, AuditIdentityKey.ID, id.toString(), null,
                    null, userId,null);
        }
        else
            return;

        auditRepository.logActivity(audit);
    }

    public void logActivity(BaseModel newObject, BaseModel oldObject) {

        List<String> auditFields = Arrays.asList("Code", "Name");
        Long userId = newObject.getCreatedBy() != null ? newObject.getCreatedBy() : newObject.getModifiedBy();

        auditFields.stream().forEach(field -> {
            if (!getFieldValue(newObject, field)
                    .equals(getFieldValue(oldObject, field))) {

                Audit audit = new Audit(newObject.getClass(), AuditAction.valueOf("CHANGE_"+field.toUpperCase()),
                        AuditIdentityKey.CODE, //indentity
                        getFieldValue(newObject, "Code"), //IndentityValue
                        getFieldValue(oldObject, field), //old value
                        getFieldValue(newObject, field), //new value
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

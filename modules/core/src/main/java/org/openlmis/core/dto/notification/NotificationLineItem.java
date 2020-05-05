package org.openlmis.core.dto.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_EMPTY;

@Data
@NoArgsConstructor
@JsonSerialize(include = NON_EMPTY)
@EqualsAndHashCode(callSuper = false)
public abstract class NotificationLineItem extends BaseModel {

    @JsonIgnore
    abstract public String getValue(String columnName) throws NoSuchFieldException, IllegalAccessException;


    public abstract Object getValueFor(String name);
}

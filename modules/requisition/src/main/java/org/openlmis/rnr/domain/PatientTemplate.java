package org.openlmis.rnr.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class PatientTemplate extends Template {

    public PatientTemplate(Long programId, List<? extends Column> listOfColumns) {
        super(programId, listOfColumns);
    }
}

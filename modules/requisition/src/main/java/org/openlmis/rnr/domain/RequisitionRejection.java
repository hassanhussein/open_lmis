package org.openlmis.rnr.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.User;
import org.openlmis.core.dto.RejectionCategoryDTO;
import org.openlmis.core.dto.RejectionReasonDTO;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_EMPTY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(include = NON_EMPTY)
@EqualsAndHashCode(callSuper = false)
public class RequisitionRejection extends BaseModel {

    private Long rnrId;

    private RejectionCategoryDTO rejectionCategory;

    private RejectionReasonDTO reason;

    private User author;


}

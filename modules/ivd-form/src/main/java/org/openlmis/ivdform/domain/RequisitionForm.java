package org.openlmis.ivdform.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.serializer.DateDeserializer;

import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RequisitionForm extends BaseModel {
    private Long periodId;
    private Long programId;
    private String orderId;
    private Long facilityId;
    private Long userId;
    private Long supervisoryNodeId;
    Boolean emergency=false;
    String description;
    String programCode;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @JsonDeserialize(using= DateDeserializer.class)
    Date requestedDeliveryDateTime;
    String status;
    private List<RequisitionProductForm> requisitionList;
}

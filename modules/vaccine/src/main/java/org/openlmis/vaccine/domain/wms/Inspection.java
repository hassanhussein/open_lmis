package org.openlmis.vaccine.domain.wms;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.User;
import org.openlmis.core.serializer.DateDeserializer;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inspection extends BaseModel {

    private Receive receive;

    private Long receiveId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date inspectionDate;

    private String inspectionNote;

    private String inspectedBy;

    private String status;

    private String receiptNumber;

    private String descriptionOfInspection;

    private User user;

    private List<InspectionLineItem> lineItems;

    //Additional Columns

    private Boolean isShippedProvided;

    private Boolean isShipped;

    private String shippedComment;

    private String shippedProvidedComment;

    private String conditionOfBox;

    private String labelAttachedComment;

}

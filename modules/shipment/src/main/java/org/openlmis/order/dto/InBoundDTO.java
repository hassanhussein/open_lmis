package org.openlmis.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_EMPTY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(include = NON_EMPTY)
public class InBoundDTO extends BaseModel implements Importable {

    @ImportField(mandatory = true, name = "PO Number")
    private String poNumber;

    @ImportField(mandatory = true, name = "Expected Arrival Date")
    private String expectedArrivalDate;

    private String status;

    @ImportField(mandatory = true, name = "Receiving Location/Storage")
    private String receivingLocationCode;

    private InBoundLineItemDTO lineItem;

}

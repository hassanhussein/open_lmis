package org.openlmis.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.message.OpenLmisMessage;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

@JsonSerialize(include = NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class BudgetDTO extends BaseModel {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, OpenLmisMessage> errorMap = new HashMap<>();

    private String facilityCode;
    private String programCode;
    private String periodStartDate;
    private String sourceApplication;
    private Long facilityId;
    private String receivedDate;
    private String UID;

    private List<BudgetLineItemDTO> lineItem = new ArrayList<>();

    private ELMISResponseMessageDTO responseMessage;

    public void validate() {
        if (isBlank(this.facilityCode) || isBlank(this.periodStartDate) || this.getLineItem().isEmpty()) {
            return;
        }
    }

    public void validateLineItem(){

        for (BudgetLineItemDTO lineItemDTO : this.getLineItem()){
            if(isBlank(lineItemDTO.getAllocatedBudget()) ||
              (Long.valueOf(lineItemDTO.getAllocatedBudget()) < 0L) || isBlank(lineItemDTO.getFundSourceCode())){
                return;
            }
        }
    }

    private void returnNullOrEmptyErrorMessage() {
      //  errorMap.put("code", new OpenLmisMessage(responseMessage.getCode()));
      //  errorMap.put("description", new OpenLmisMessage(responseMessage.getDescription()));
       // errorMap.put("usage", new OpenLmisMessage(responseMessage.getUsage()));

    }

    public Map<String, OpenLmisMessage> validateBudgetFields() {
        validate();
        validateLineItem();
        return errorMap;
    }

    public Map<String, OpenLmisMessage> validateNull(Facility facility) {
        if(facility == null){
            returnNullOrEmptyErrorMessage();
        }
        return errorMap;
    }

    public Map<String, OpenLmisMessage>  setSuccess() {
        errorMap.put("success", new OpenLmisMessage(responseMessage.getCode(),responseMessage.getDescription()));
        return errorMap;
    }

    public Map<String, OpenLmisMessage>  setFailure() {
        errorMap.put("Fail", new OpenLmisMessage(responseMessage.getCode(),responseMessage.getDescription()));
        return errorMap;
    }
}

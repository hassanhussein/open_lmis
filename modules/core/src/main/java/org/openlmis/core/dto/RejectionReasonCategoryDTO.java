package org.openlmis.core.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;

import java.util.List;

@Data
public class RejectionReasonCategoryDTO  extends BaseModel {

    private String name;

    private String code;

    private List<RejectionReasonDTO> rejections;


}

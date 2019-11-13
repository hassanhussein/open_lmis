package org.openlmis.rnr.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;

import java.util.ArrayList;
import java.util.List;

@Data
public class RejectionDTO extends BaseModel {

    private List<ReasonDTO> rejections = new ArrayList<>();

    private String name;

    private String code;

    private Long rnrId;

}

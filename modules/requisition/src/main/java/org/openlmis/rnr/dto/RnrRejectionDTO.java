package org.openlmis.rnr.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;

import java.util.ArrayList;
import java.util.List;

@Data
public class RnrRejectionDTO extends BaseModel {
   Long  rnrId;

   List<RejectionDTO> rejections = new ArrayList<>();

}

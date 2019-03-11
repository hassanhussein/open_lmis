package org.openlmis.pod.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PodDTO {


    private List<PodLineItemDTO> podLineItems = new ArrayList<>();

}

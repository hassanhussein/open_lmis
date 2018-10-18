package org.openlmis.restapi.domain;

import lombok.Data;

import java.util.List;

@Data
public class CustomResponseMessage {

private String sourceOrderId;
private Long rnrId;
List<MessageDTO>message;

}

package org.openlmis.core.dto;

import lombok.Data;

@Data
public class ImportCountDTO {

  Long imported;
  Long updated;
  Long ignored;
  Long deleted;
}

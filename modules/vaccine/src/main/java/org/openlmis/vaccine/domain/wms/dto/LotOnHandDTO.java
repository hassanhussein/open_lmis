package org.openlmis.vaccine.domain.wms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.openlmis.core.serializer.DateDeserializer;

import java.util.Date;

@Data
public class LotOnHandDTO {

    private Long lotId;

    private String number;

    private Long amount;

    private String vvm;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date expiry;

    private Integer id;

    private Long maxSoh;

}

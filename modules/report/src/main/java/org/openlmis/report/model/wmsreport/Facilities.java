package org.openlmis.report.model.wmsreport;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Facilities {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    int id;
    String code;
    String name;

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

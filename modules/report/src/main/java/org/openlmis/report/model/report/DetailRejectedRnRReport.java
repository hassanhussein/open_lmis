package org.openlmis.report.model.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.report.model.ResultRow;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailRejectedRnRReport implements ResultRow {
    private Long rnrid;
    private String status;
    private Date  createddate;
    private String  createdby;
    private Date modifieddate;
    private Long modifiedby;
    private String  emergency;
    private String clientsubmittedtime;
    private String clientsubmittednotes;
    private String sourceapplication;
    private Long zoneid;
    private String parent;
    private String  district;
    private Long districtid;
    private Long provinceid;
    private String province;
    private String facility;
    private Long facilitytypeid;
    private String facilitytype;
    private String program;
    private Long  programid;
    private Long  periodid;
    private String period;
    private Date startdate;
    private Date enddate;
    private String facilitycode;
    private Long  facilityid;
    private Date  rejectiondate;
    private String  statuschange;
    private String commenttext;
}

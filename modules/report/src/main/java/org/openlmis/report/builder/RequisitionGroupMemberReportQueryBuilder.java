
/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openlmis.report.builder;


import org.openlmis.report.model.params.RequisitionGroupParam;
import org.openlmis.report.model.params.UserSummaryParams;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class RequisitionGroupMemberReportQueryBuilder {


    public static String getQuery(Map filter){
        String query= "";
        RequisitionGroupParam param=(RequisitionGroupParam)filter.get("filterCriteria");
//        Map sortCriteria = (Map)filter.get("SortCriteria");
        BEGIN();
        SELECT("p.id programid");
        SELECT(" p.code");
        SELECT("p.name \"program\"");
        SELECT("ps.id scheduleid");
        SELECT("ps.name schedule");
        SELECT("rg.id requisitionid");
        SELECT("rg.name groupname");
        SELECT("f.id facilityid");
        SELECT("f.name facility ");
        FROM("requisition_group_program_schedules rgs");
        INNER_JOIN("requisition_group_members rgm on rgs.requisitiongroupid = rgm.requisitiongroupid");
        INNER_JOIN("processing_schedules ps ON ps.id = rgs.scheduleid");
        INNER_JOIN("requisition_groups rg on rgm.requisitiongroupid=rg.id");
        INNER_JOIN("programs p on rgs.programid=p.id");
        INNER_JOIN("facilities f on rgm.facilityid=f.id");
        ORDER_BY("rg.name,p.name,ps.name,f.name");
        writePredicate(param);
        query=SQL();
        return query;
    }

    private static void writePredicate(RequisitionGroupParam param){
        if(param.getProgram()!=null&& !param.getProgram().equals(0L)){
            WHERE("p.id="+param.getProgram());
        }
        if(param.getSchedule()!=null&& !param.getSchedule().equals(0L)){
            WHERE("ps.id="+param.getSchedule());
        }
        if(param.getGroup()!=null&& !param.getGroup().equals(0L)){
            WHERE("rg.id="+param.getGroup());
        }
        if(param.getFacility()!=null&& !param.getFacility().equals(0L)){
            WHERE("f.id="+param.getFacility());
        }
        ;
    }
}

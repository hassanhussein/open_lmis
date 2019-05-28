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

package org.openlmis.report.model.report;

import lombok.*;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.dto.OrderFillRate;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderFillRateReport implements ResultRow {
    private Long rnrid;
    private String facility;
    private String facilitycode;
    private Integer approved;
    private Integer receipts;
    private String productcode;
    private String product;
    private Integer item_fill_rate;
    private String ORDER_FILL_RATE;
    private String facilityType;
    private String supplyingFacility;
    private String category;
    private Integer totalproductsreceived;
    private Integer totalproductsapproved;
    private String name;
    private String nameLabel;
    private String count;
    private String substitutedProductCode;
    private String substitutedProductName;
    private Double substitutedProductQuantityShipped;
    private Double substitutedquantityshipped;
    private Double order;
    private String province;
    private String provinceid;
    private String district;
    private String districtid;
    private String programid;
    private String program;
    private String periodid;
    private String period;
    private Date shippeddate;
    private Date packeddate;
    private List<OrderFillRateReport> substituteProductList;

}

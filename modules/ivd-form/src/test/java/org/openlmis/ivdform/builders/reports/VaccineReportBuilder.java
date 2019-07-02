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

package org.openlmis.ivdform.builders.reports;

import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;
import com.natpryce.makeiteasy.PropertyLookup;
import org.openlmis.core.builder.ProcessingPeriodBuilder;
import org.openlmis.core.domain.ProcessingPeriod;
import org.openlmis.ivdform.domain.reports.ReportStatus;
import org.openlmis.ivdform.domain.reports.VaccineReport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.natpryce.makeiteasy.MakeItEasy.*;
import static com.natpryce.makeiteasy.Property.newProperty;
import static org.openlmis.core.builder.ProcessingPeriodBuilder.defaultProcessingPeriod;

public class VaccineReportBuilder {

  public static final Property<VaccineReport, Long> periodId = newProperty();
  public static final Instantiator<VaccineReport> defaultVaccineReport = new Instantiator<VaccineReport>() {

    @Override
    public VaccineReport instantiate(PropertyLookup<VaccineReport> lookup) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date processingPeriodEndDate = null;
      Date todayDate = null;
      try {
        processingPeriodEndDate = dateFormat.parse("2019-03-31");
        todayDate = new Date();
      } catch (ParseException e) {
        e.printStackTrace();
      }

      VaccineReport item = new VaccineReport();
      item.setProgramId(5L);
      item.setFacilityId(1L);
      item.setPeriodId(1L);
      Long processingPeriodId = lookup.valueOf(periodId, 1L);
      ProcessingPeriod period =
              make(a(defaultProcessingPeriod, with(ProcessingPeriodBuilder.id, processingPeriodId)));
      period.setEndDate(processingPeriodEndDate);
      item.setPeriod(period);
      item.setSubmissionDate(todayDate);
      item.setSupervisoryNodeId(1L);
      item.setStatus(ReportStatus.DRAFT);
      return item;
    }
  };
}

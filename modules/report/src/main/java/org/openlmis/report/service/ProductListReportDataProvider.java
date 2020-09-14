package org.openlmis.report.service;

import lombok.NoArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.report.mapper.ProductListReportMapper;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Component
@NoArgsConstructor
public class ProductListReportDataProvider extends ReportDataProvider {

   @Autowired
   private ProductListReportMapper mapper;

    @Autowired
    private SelectedFilterHelper filterHelper;

    @Override
    @Transactional
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {
        RowBounds rowBounds = new RowBounds((page - 1) * pageSize, pageSize);
        return mapper.getFilteredSortedProductListReport(filterCriteria, rowBounds,this.getUserId());
    }

    @Override
    @Transactional
    public String getFilterSummary(Map<String, String[]> params) {
        return  filterHelper.getProgramPeriodGeoZone(params);
    }
}

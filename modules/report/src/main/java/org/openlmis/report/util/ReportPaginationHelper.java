package org.openlmis.report.util;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ReportPaginationHelper {


    private Integer pageSize;

    @Autowired
    public void setPageSize(@Value("${search.page.size}") String pageSize) {
        this.pageSize = Integer.parseInt(pageSize);
    }

    public Pagination getPagination(Integer page) {
        return new Pagination(page, pageSize);
    }

}

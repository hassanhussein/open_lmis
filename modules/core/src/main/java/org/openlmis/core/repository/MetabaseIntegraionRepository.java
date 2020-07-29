
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
package org.openlmis.core.repository;


import lombok.NoArgsConstructor;
import org.openlmis.core.domain.MetabaseMenu;
import org.openlmis.core.domain.MetabasePage;
import org.openlmis.core.repository.mapper.MetabaseIntegraionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@NoArgsConstructor
public class MetabaseIntegraionRepository {
    @Autowired
    MetabaseIntegraionMapper mapper;

    public void addMetabaseMenu(MetabaseMenu metabaseMenu) {
        if (metabaseMenu.getId() != null && !metabaseMenu.getId().equals(0L))
            mapper.modifyMenu(metabaseMenu);
        else
            mapper.insert(metabaseMenu);
    }

    public void addMetabasePage(MetabasePage metabasePage) {
        if (metabasePage.getId() != null && !metabasePage.getId().equals(0L))
            mapper.updatePage(metabasePage);
        else
            mapper.insertPage(metabasePage);
    }

    public List<MetabaseMenu> loadMetabaseMenuList() {
        return mapper.loadMetabaseMenus();
    }

    public List<MetabasePage> loadMetabasePageList() {
        return mapper.loadMetabasePages();
    }

    public void deletePage(MetabasePage metabasePage) {
        this.mapper.removePage(metabasePage);
    }

    public void modifyMetabasePage(MetabasePage metabasePage) {
        this.mapper.updatePage(metabasePage);
    }

    public void removeMetabaseMenu(MetabaseMenu metabaseMenu) {
        this.mapper.deleteMenu(metabaseMenu);
    }

    public void updateMetabaseMenu(MetabaseMenu metabaseMenu) {
        this.mapper.modifyMenu(metabaseMenu);
    }

    public List<MetabasePage> loadMenuPageList(MetabaseMenu menu) {
        return this.mapper.loadMenuPageList(menu);
    }
}

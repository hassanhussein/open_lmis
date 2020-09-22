
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
package org.openlmis.core.service;


import lombok.NoArgsConstructor;
import org.openlmis.core.domain.MetabaseItem;
import org.openlmis.core.domain.MetabaseMenu;
import org.openlmis.core.domain.MetabasePage;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.repository.MetabaseIntegraionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@NoArgsConstructor
public class MetabaseIntegraionService {
    @Autowired
    MetabaseIntegraionRepository repository;
    private List<MetabaseMenu> metabaseMenuList;

    public void addMetabaseMenu(MetabaseMenu metabaseMenu) {

        repository.addMetabaseMenu(metabaseMenu);
    }

    public void addMetabasePage(MetabasePage metabasePage) {
        if(!validateMenu(metabasePage)){
            throw  new DataException("error.invalid.hierarchy");
        }
        repository.addMetabasePage(metabasePage);
    }

    public List<MetabaseMenu> loadMetabaseMenuList() {
        this.metabaseMenuList = repository.loadMetabaseMenuList();
        List<MetabaseMenu> menuList = this.getRootMenus();
        menuList.stream().forEach(m -> {
            m.setMenuItem(true);
            buildMenuTree(m);
        });

        return menuList;
    }

    public List<MetabaseMenu> getRootMenus() {
        return metabaseMenuList.stream().filter(m -> m.getParentMenu() == null).collect(Collectors.toList());
    }

    public List<MetabaseItem> getMenuItems(MetabaseMenu menu) {
        return metabaseMenuList.stream().filter(m ->
                m.getParentMenu() != null && m.getParentMenu().equals(menu.getId())).collect(Collectors.toList());
    }

    public void buildMenuTree(MetabaseMenu menu) {
        List<MetabaseItem> items = getMenuItems(menu);
        List<MetabasePage> pageItemList = this.repository.loadMenuPageList(menu);
        if (items != null && !items.isEmpty()) {
            items.stream().forEach(i -> {
                i.setMenuItem(true);
                buildMenuTree((MetabaseMenu) i);
            });

        } else {
            items = new ArrayList<>();
        }
        if (pageItemList != null && !pageItemList.isEmpty()) {
            items.addAll(pageItemList);
        }
        if (items != null && !items.isEmpty()) {
            menu.setItems(items);
        }

    }

    public List<MetabasePage> loadMetabasePageList() {
        return repository.loadMetabasePageList();
    }

    public void removeMetabasePage(MetabasePage metabasePage) {
        this.repository.deletePage(metabasePage);
    }

    public void modifyMetabasePage(MetabasePage metabasePage) {
        this.repository.modifyMetabasePage(metabasePage);
    }

    public void removeMetabaseMenu(MetabaseMenu metabaseMenu) {
        this.repository.removeMetabaseMenu(metabaseMenu);
    }

    public void updateMetabaseMenu(MetabaseMenu metabaseMenu) {
        this.repository.updateMetabaseMenu(metabaseMenu);
    }

    public List<MetabaseMenu> loadFlatMetabaseMenuList() {
        return this.repository.loadMetabaseMenuList();
    }

    public boolean validateMenu(MetabasePage page) {
        boolean isValid = true;
        if(!page.isMenuItem() && (page.getMenu()==null || page.getMenu().getId()==null || page.getMenu().getId().equals(0l))){
            isValid=false;
        }
        return isValid;
    }
}

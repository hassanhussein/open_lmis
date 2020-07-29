
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
package org.openlmis.core.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.MetabaseMenu;
import org.openlmis.core.domain.MetabasePage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetabaseIntegraionMapper {


    @Insert("INSERT INTO embed_menu(name,parentMenu,icon," +
            "menuitem,  description, createdDate,createdBy, modifiedBy, modifiedDate) " +
            "VALUES(#{name}, #{parentMenu}, #{icon}, #{menuBarItem}, #{description}," +
            " COALESCE(#{createdDate}, NOW()), #{createdBy}, #{modifiedBy}, " +
            "COALESCE(#{modifiedDate}, CURRENT_TIMESTAMP))")
    @Options(useGeneratedKeys = true)
    Integer insert(MetabaseMenu metabaseMenu);

    @Select(" select * from embed_menu")
    public List<MetabaseMenu> loadMetabaseMenus();

    @Delete("Delete from embed_menu where id = #{id} ")
    void deleteMenu(MetabaseMenu menu);

    @Update(" update embed_menu " +
            " set name = #{name} ," +
            "     parentMenu =#{parentMenu}," +
            "     icon = #{icon}," +
            "     menuitem=#{menuBarItem}," +
            "     description= #{description}," +
            "     modifiedDate= #{modifiedDate}" +
            "     modifiedBy=#{modifiedBy}")
    void modifyMenu(MetabaseMenu menu);


    @Insert("INSERT INTO metabase_page(name,menuid,icon," +
            "linkurl,  description,rights, createdDate,createdBy, modifiedBy, modifiedDate) " +
            "VALUES(#{name}, #{menu.id}, #{icon}, #{linkUrl}, #{rights}, #{description}," +
            " COALESCE(#{createdDate}, NOW()), #{createdBy}, #{modifiedBy}, " +
            "COALESCE(#{modifiedDate}, CURRENT_TIMESTAMP))")
    @Options(useGeneratedKeys = true)
    Integer insertPage(MetabasePage metabasePage);

    @Select(" select * from metabase_page")
    public List<MetabasePage> loadMetabasePages();

    @Delete("Delete from metabase_page where id = #{id} ")
    void removePage(MetabasePage page);

    @Update(" update metabase_page " +
            " set name = #{name} ," +
            "     menuid =#{menu.id}," +
            "     icon = #{icon}," +
            "     urllink=#{linkUrl}," +
            "     rights= #{rights}," +
            "     description= #{description}," +
            "     modifiedDate= #{modifiedDate}" +
            "     modifiedBy=#{modifiedBy}")
    void updatePage(MetabasePage page);
    @Select(" select * from metabase_page where menuid=#{id}")
    List<MetabasePage> loadMenuPageList(MetabaseMenu menu);
}

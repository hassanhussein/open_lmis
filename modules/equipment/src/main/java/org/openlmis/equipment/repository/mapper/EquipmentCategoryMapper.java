package org.openlmis.equipment.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.Facility;
import org.openlmis.equipment.domain.Equipment;
import org.openlmis.equipment.domain.EquipmentCategory;
import org.openlmis.equipment.domain.EquipmentType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentCategoryMapper {

    @Select("select *  from equipment_category")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "functionalTestTypeIds", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.equipment.repository.mapper.EquipmentFunctionalTestTypesMapper.getByEquipmentCategoryId")),
            @Result(property = "equipmentTypeIds", javaType = List.class, column = "id",
                    many = @Many(select = "getEquipmentTypeByCategoryId")),
    })
    List<EquipmentCategory> getAllEquipmentCategory();

    @Select("select * from equipment_category where id = #{id}")
    EquipmentCategory getEquipmentCategoryById(Long id);

    @Delete("delete from equipment_category where id = #{id}")
    void deleteEquipmentCategory(Long id);

    @Update("update equipment_category set code= #{code} , name= #{name} where id=#{id}")
    void updateEquipmentCategory(EquipmentCategory obj);

    @Insert("insert into equipment_category (code, name) values (#{code} , #{name})")
    void insertEquipmentCategory(EquipmentCategory obj);

    @Select("select id from equipment_types where categoryid = #{id}")
    List<Long> getEquipmentTypeByCategoryId(Long id);

    @Update("update equipment_types set categoryid = #{equipemntCategoryId} where id = #{equipmentTypeId}")
    void associateEquipmentTypes(@Param("equipemntCategoryId") Long equipemntCategoryId, @Param("equipmentTypeId") Long equipmentTypeId);

    @Update("update equipment_types set categoryid = null")
    void resetEquipmentTypecategoryAssociation();

   @Update("update equipment_functional_test_types set equipmentcategoryid = null")
    void resetEquipmentFunctionalTestTypeCategoryAssociation();

    @Update("update equipment_functional_test_types set  equipmentcategoryid = #{equipmentCategoryId} where id = #{functionalTestTypeId}")
    void associateFunctionalTestTypes(@Param("equipmentCategoryId") Long equipmentCategoryId, @Param("functionalTestTypeId") Long functionalTestTypeId);

    @Select("select * from equipment_category where code = #{code}")
    EquipmentCategory getByCode(String code);

    @Update("UPDATE equipment_category SET " +
            "name = #{name}, code = #{code}, modifiedBy = #{modifiedBy}, modifiedDate = NOW()" +
            " WHERE id = #{id}")
    void update(EquipmentCategory category);

    @Insert("insert into equipment_category (code, name, createdBy, createdDate, modifiedBy, modifiedDate) " +
            " values " +
            " (#{code}, #{name}, #{createdBy},COALESCE(#{createdDate}, NOW()), #{modifiedBy}, NOW())")
    void insert(EquipmentCategory category);
}

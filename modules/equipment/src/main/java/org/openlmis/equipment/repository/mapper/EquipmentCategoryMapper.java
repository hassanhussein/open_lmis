package org.openlmis.equipment.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.Facility;
import org.openlmis.equipment.domain.Discipline;
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
            @Result(property = "discipline", javaType = Long.class, column = "disciplineId",
                    one = @One(select = "getDisciplineById")),


    })
    List<EquipmentCategory> getAllEquipmentCategory();

    @Select("select * from equipment_category where id = #{id}")
    EquipmentCategory getEquipmentCategoryById(Long id);

    @Delete("delete from equipment_category where id = #{id}")
    void deleteEquipmentCategory(Long id);

    @Update("update equipment_category set disciplineId=#{disciplineId}, code= #{code} , name= #{name} where id=#{id}")
    void updateEquipmentCategory(EquipmentCategory obj);

    @Insert("insert into equipment_category (code, name, disciplineId) values (#{code} , #{name}, #{disciplineId})")
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

    @Select(" SELECT * FROM equipment_category WHERE lower(code) = lower(#{code})")
    EquipmentCategory getByCode(@Param("code") String code);

    @Select(" Select  * from disciplines ")
    List<Discipline> getAllDisciplines();

    @Insert("Insert into disciplines (code, name, description) " +
            " VALUES (#{code}, #{name}, #{description})")
    @Options(useGeneratedKeys = true)
    Integer insertDiscpline(Discipline discipline);

    @Update("update disciplines set code = #{code}, name=#{name}, description =#{description} where id = #{id}")
    void UpdateDiscipline(Discipline discipline);

    @Select(" SELECT * FROM disciplines WHERE code = #{code} ")
    Discipline getDisciplineByCode(@Param("code") String code);

    @Select(" SELECT * FROM disciplines WHERE id = #{id} ")
    Discipline getDisciplineById(@Param("id") Long id);

}

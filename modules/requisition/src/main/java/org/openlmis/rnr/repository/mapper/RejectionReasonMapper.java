package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.dto.RejectionCategoryDTO;
import org.openlmis.core.dto.RejectionReasonCategoryDTO;
import org.openlmis.rnr.domain.RejectionReason;
import org.openlmis.core.dto.RejectionReasonDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RejectionReasonMapper {

    @Insert(" INSERT INTO public.requisition_rejections(\n" +
            "            rnrId, rejectionCategoryId, rejectionId, createdBy, createddate, \n" +
            "            modifiedBy, modifiedDate)\n" +
            "    VALUES (#{rnrId}, #{rejectionCategory.id}, #{rejection.id}, #{createdBy}, NOW(), \n" +
            "            #{modifiedBy}, NOW());")
    @Options(useGeneratedKeys = true)
    int insert(RejectionReason rejectionReason);

    @Select("SELECT * FROM rejection_reasons WHERE rnrId = #{rnrId} ORDER BY createdDate")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "author.id", column = "createdBy"),

            @Result(
                    property = "rejection.id",
                    column = "rejectionId",
                    javaType = Long.class,
                    one = @One(
                            select = "org.openlmis.core.repository.mapper.RejectionReasonDTOMapper.getById"
                    )
            ),
            @Result(
                    property = "rejectionCategory.id",
                    column = "rejectionCategoryId",
                    javaType = Long.class,
                    one = @One(
                            select = "org.openlmis.core.repository.mapper.RejectionCategoryMapper.getById"
                    )
            )

    })
    List<RejectionReason> getByRnrId(Long rnrId);

    @Select("select * from rejections ")
    List<Map<String,Object>>getAllRejections();

    @Insert("INSERT INTO public.rejection_reasons(\n" +
            "            rnrid, name, code, createdBy, createdDate, modifiedBy, modifiedDate)\n" +
            "    VALUES ( #{rnrId}, #{name}, #{code}, #{createdBy},NOW(), #{modifiedBy}, NOW());")
    @Options(useGeneratedKeys = true)
    int insertUploaded(RejectionReasonDTO rejectionReason);

    @Update("update rejection_reasons set name = #{name}, code = #{code} where id = #{id}")
    void update(RejectionReasonDTO rejectionReasonDTO);


    @Select(" select * from rejection_categories ")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(
                    property = "rejections",
                    column = "id",
                    javaType = List.class,
                    many = @Many(
                            select = "org.openlmis.core.repository.mapper.RejectionReasonDTOMapper.getByRejectionCategory"
                    )
            )

    })
    List<RejectionReasonCategoryDTO>getRejectionByCategory();


    @Select("SELECT * FROM requisition_rejections s\n" +
            "JOIN rejections r on s.rejectionId = r.id\n" +
            "where rnrid = #{rnrId}")

    List<RejectionReasonDTO> getRejectionsByRnrId(@Param("rnrId") Long rnrId);


}

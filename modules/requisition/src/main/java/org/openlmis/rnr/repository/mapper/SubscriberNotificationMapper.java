package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberNotificationMapper {

    @Insert("INSERT INTO support_bot_subscribers (rnrId, chatId, label, source, active) VALUES " +
            " (#{rnrId}, #{chatId}, #{label}, #{source}, true )")
    @Options(useGeneratedKeys = false)
    void insert(@Param("chatId") String chatId, @Param("rnrId") Integer rnrId, @Param("label") String label, @Param("source") String source);
}

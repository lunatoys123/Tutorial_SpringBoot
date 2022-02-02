package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    List<Message> selectConversation(int userId, int offset, int limit);

    int selectConversationCount(int userId);

    List<Message> selectLetters(String conversationId, int offset, int limit);

    int selectLetterCount(String conversationId);

    int selectLetterUnreadCount(int userId, String conversationId);

    int insertMessage(Message message);

    int updateStatus(List<Integer> ids, int status);

    Message SelectLatestNotice(int userId, String topic);

    int selectNoticeCount(int userId, String topic);

    int selectNoticeUnreadCont(int userId, String topic);

    List<Message> selectNotices(int userId, String topic, int offset, int limit);

}

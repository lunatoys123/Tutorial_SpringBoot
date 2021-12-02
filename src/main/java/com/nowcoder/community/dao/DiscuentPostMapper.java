package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscuentPostMapper {

    List<DiscussPost> selectDiscussPost(int userId, int offset, int limit);
    //@param use when only one parameter
    int selectDiscussPostRows(@Param("userId") int userId);



}

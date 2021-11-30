package com.nowcoder.community.Service;

import com.nowcoder.community.dao.DiscuentPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscuentPostMapper discuentPostMapper;

    public List<DiscussPost> findDiscussPost(int userId, int offset, int limit){
        return discuentPostMapper.selectDiscussPost(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId){
        return discuentPostMapper.selectDiscussPostRows(userId);
    }
}

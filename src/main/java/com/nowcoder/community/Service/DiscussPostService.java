package com.nowcoder.community.Service;

import com.nowcoder.community.dao.DiscuentPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscuentPostMapper discuentPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<DiscussPost> findDiscussPost(int userId, int offset, int limit){
        return discuentPostMapper.selectDiscussPost(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId){
        return discuentPostMapper.selectDiscussPostRows(userId);
    }

    public int AddDiscussPost(DiscussPost post){
        if(post == null){
            throw new IllegalArgumentException("post cannot be null");
        }

        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));

        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discuentPostMapper.insertDiscussPost(post);
    }

    public DiscussPost findDiscussPostById(int id){
        return discuentPostMapper.selectDiscussPostById(id);
    }

    public int updateCommentCount(int id, int commentCount){
        return discuentPostMapper.updateCommentCount(id, commentCount);
    }
}

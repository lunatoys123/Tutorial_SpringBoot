package com.nowcoder.community.Service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class AlphaService {
    @Autowired
    private AlphaDao alphaDao;

    public AlphaService(){
        System.out.println("construct AlphaService");
    }

    @PostConstruct
    public void init(){
        System.out.println("Initial AlphaService");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("Destroy AlphaService");
    }

    public String find(){
        return alphaDao.select();
    }
}

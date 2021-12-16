package com.nowcoder.community.Service;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private LoginTicketMapper loginTicketMapper;


    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    public Map<String, Object> register(User user) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();

        if(user==null){
            throw new IllegalAccessException("parameter cannot be null");
        }

        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","username cannot be null");
            return map;
        }

        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","password cannot be null");
            return map;
        }

        if(StringUtils.isBlank(user.getEmail())){
            map.put("EmailMsg","Email cannot be null");
            return map;
        }

        //check if user exist

        User u = userMapper.selectByName(user.getUsername());
        if(u != null){
            map.put("usernameMsg","User already exists");
            return map;
        }

        u = userMapper.selectByEmail(user.getEmail());
        if(u!=null){
            map.put("EmailMsg", "Email already exists");
            return map;
        }

        // Register user
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head.%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());

        userMapper.insertuser(user);

        //Activation email
        Context context = new Context();
        context.setVariable("email", user.getEmail());

        String url = domain+contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation", context);

        mailClient.sendMail(user.getEmail(), "Activation Account", content);
        return map;
    }

    public int activation(int userId, String code){
        User user = userMapper.selectById(userId);
        if(user.getStatus() == 1){
            return ACTIVATION_REPEAT;
        }else if(user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        }else{
            return ACTIVATION_FAILURE;
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds){
        Map<String, Object> map = new HashMap<>();

        if(StringUtils.isBlank(username)){
            map.put("usernameMsg", "username cannot be null");
            return map;
        }

        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","password cannot be null");
            return map;
        }

        //check username
        User user = userMapper.selectByName(username);
        System.out.println(user);
        if(user==null){
            map.put("usernameMsg", "username not exist");
            return map;
        }

        //check status
        if(user.getStatus() == 0){
            map.put("usernameMsg", "user not activate");
            return map;
        }

        //check password
        password = CommunityUtil.md5(password+user.getSalt());
        System.out.println(password);
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg","wrong password");
            return map;
        }

        // generate login certificate
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket, 1);
    }

    public LoginTicket findLoginTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);
    }

    public int updateHeader(int userId, String headerUrl){
        return userMapper.updateHeader(userId, headerUrl);
    }

    public User findUserByName(String username){
        return userMapper.selectByName(username);
    }
}

package com.wei.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wei.common.R;
import com.wei.entity.EmailDetails;
import com.wei.entity.User;
import com.wei.service.EmailService;
import com.wei.service.UserService;
import com.wei.utils.ValidateCodeUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description: TODO
 * @author: Wei Liang
 * @date: 2023年04月07日 10:23 PM
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    /**
     * send verification code through email
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //get email
        String email = user.getEmail();

        if (StringUtils.isNotEmpty(email)){
            //generate random code
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            //send email
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setSubject("verification code");
            emailDetails.setBody("code is {}"+code);
            emailDetails.setRecipient(email);
            boolean sendMail = emailService.sendMail(emailDetails);
            if(!sendMail){
                return R.error("could not send email");
            }

            //store random number to session
            session.setAttribute(email,code);
        }
        return R.success("code sent");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        //get email
        String email = map.get("email").toString();

        //get code
        String code = map.get("code").toString();

        //get session's code
        Object codeInSession = session.getAttribute(email);

        //compare
        if(codeInSession!=null && codeInSession.equals(code)){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(User::getEmail,email);

            User user = userService.getOne(queryWrapper);

            //if user is null, then it means it is a new user
            //and auto register for they
            if(user == null){
                user = new User();
                user.setEmail(email);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("cannot login");
    }
}

package com.vahidsaghlatoon.homeservicesystem.utils;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

public class Utility {

    public static String getSiteURL(HttpServletRequest request){
        String siteURL = request.getRequestURL().toString();
        return  siteURL.replace(request.getServletPath(),"");
    }

    public static JavaMailSenderImpl prepareMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("vahidsaghlatoon66@gmail.com");
        mailSender.setPassword("owxegxkmcuqkdocs");

        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.auth","true");
        mailProperties.setProperty("mail.smtp.starttls.enable","true");

        mailSender.setJavaMailProperties(mailProperties);
        return mailSender;
    }
}

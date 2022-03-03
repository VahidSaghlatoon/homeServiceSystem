package com.vahidsaghlatoon.homeservicesystem.controller;

import com.vahidsaghlatoon.homeservicesystem.model.Credit;
import com.vahidsaghlatoon.homeservicesystem.model.Customer;
import com.vahidsaghlatoon.homeservicesystem.model.Role;
import com.vahidsaghlatoon.homeservicesystem.model.UserState;
import com.vahidsaghlatoon.homeservicesystem.service.CustomerService;
import com.vahidsaghlatoon.homeservicesystem.service.RoleService;
import com.vahidsaghlatoon.homeservicesystem.utils.FileUploadUtil;
import com.vahidsaghlatoon.homeservicesystem.utils.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {
    private final CustomerService customerService ;
    private  final RoleService roleService ;

    @GetMapping("/customer")
    public String getCustomer(Model theModel){
        theModel.addAttribute("customer",new Customer());
        return "/register-customer-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("customer") Customer theCustomer,
                       BindingResult theBindingResult,
                       RedirectAttributes redirectAttributes,
                       HttpServletRequest request,
                       @RequestParam("image") MultipartFile multipartFile) throws IOException, MessagingException {

        if (theBindingResult.hasErrors()) {
            return "/customers/customer-form";
        }
        Role customerRole = roleService.findById(3L);
        theCustomer.setRole(customerRole);
        theCustomer.setCredit(new Credit());
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            theCustomer.setPhoto(fileName);
            Customer savedEmployee = customerService.saveOrUpdate(theCustomer);
            String uploadDir = "../customer-photos/" + savedEmployee.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (theCustomer.getPhoto().isEmpty()) theCustomer.setPhoto(null);
            customerService.saveOrUpdate(theCustomer);
            sendVerificationEmail(request,theCustomer);
        }
        redirectAttributes.addFlashAttribute("message", "ثبت نام انجام شد لطفا ایمیل خود را بررسی کنید");
        return "redirect:/login";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer theCustomer) throws MessagingException, UnsupportedEncodingException {
        JavaMailSenderImpl mailSender = Utility.prepareMailSender();

        String toAddress = theCustomer.getEmail();
        String subject = "تایید  ثبت نام";
        String content = "\"<a href=url> click for VERIFY </a>\";";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("vahidsaghlatoon66@gmail.com" , "Home_service");
        helper.setTo(toAddress);
        helper.setSubject(subject);

        String verifyURL = Utility.getSiteURL(request) + "/register/verify?code="+ theCustomer.getVerificationCode();
        content = content.replace("url" , verifyURL);

        helper.setText(content,true);
        mailSender.send(message);
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code , Model model,RedirectAttributes attributes){
        Customer customer = customerService.findByVerificationCode(code);
        boolean verified = customerService.verify(code);
        if (verified){
            attributes.addFlashAttribute("success","ثبت نام با موفقیت انجام شد منتظر ایمیل تایید باشید ");
        }
        else {
            attributes.addFlashAttribute("fail","ثبت نام شما قبلا تکمیل شده و یا کد اعتبارسنجی نامعتبر است ");
        }
        return "redirect:/login";
    }

}

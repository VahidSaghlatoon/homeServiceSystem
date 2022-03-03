package com.vahidsaghlatoon.homeservicesystem.controller;

import com.vahidsaghlatoon.homeservicesystem.exception.NotExistException;
import com.vahidsaghlatoon.homeservicesystem.model.Credit;
import com.vahidsaghlatoon.homeservicesystem.model.Customer;
import com.vahidsaghlatoon.homeservicesystem.model.Role;
import com.vahidsaghlatoon.homeservicesystem.model.UserState;
import com.vahidsaghlatoon.homeservicesystem.service.CustomerService;
import com.vahidsaghlatoon.homeservicesystem.service.RoleService;
import com.vahidsaghlatoon.homeservicesystem.utils.FileUploadUtil;
import com.vahidsaghlatoon.homeservicesystem.utils.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import java.util.List;

@Controller
@RequestMapping(value = "/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final RoleService roleService;


    @GetMapping
    public String findAll(Model theModel) {
        return findAllByPage(1, theModel,"firstName","asc",null);
    }

    @GetMapping("/page/{pageNumber}")
    public String findAllByPage(
            @PathVariable(name = "pageNumber") int pageNumber,
            Model theModel,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyword") String keyword
                                ) {
        Page<Customer> page = customerService.findAllByPage(pageNumber,sortField,sortDir,keyword);
        List<Customer> theCustomers = page.getContent();
        long startCount = (long) (pageNumber - 1) * CustomerService.CUSTOMER_PER_PAGE + 1;
        long endCount = startCount * CustomerService.CUSTOMER_PER_PAGE;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        theModel.addAttribute("currentPage", pageNumber);
        theModel.addAttribute("keyword", keyword);
        theModel.addAttribute("totalPages", page.getTotalPages());
        theModel.addAttribute("startCount", startCount);
        theModel.addAttribute("endCount", endCount);
        theModel.addAttribute("totalItem", page.getTotalElements());
        theModel.addAttribute("theCustomers", theCustomers);
        theModel.addAttribute("sortField", sortField);
        theModel.addAttribute("sortDir", sortDir);
        theModel.addAttribute("reverseSortDir", reverseSortDir);
        return "/customers/customers";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        Customer theCustomer = new Customer();
        theCustomer.setEnabled(true);
        List<Role> theRoles = roleService.findAll();
        theModel.addAttribute("theRoles", theRoles);
        theModel.addAttribute("customer", theCustomer);
        return "/customers/customer-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("customer") Customer theCustomer,
                       BindingResult theBindingResult,
                       RedirectAttributes redirectAttributes,
                       @RequestParam("image") MultipartFile multipartFile) throws IOException {

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
        }
        redirectAttributes.addFlashAttribute("message", "مشتری جدید اضافه شد");
        return getRedirectUrlByAffectedCustomer(theCustomer);
    }

    private String getRedirectUrlByAffectedCustomer(Customer theCustomer) {
        String firstPartOfEmail = theCustomer.getEmail().split("@")[0];
        return "redirect:/customers/page/1?sortField=firstName&sortDir=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("customerId") Long theId,
                                    Model theModel, RedirectAttributes redirectAttributes) {
        try {
            Customer theCustomer = customerService.findById(theId);
            theModel.addAttribute("customer", theCustomer);
            return "/customers/customer-form";
        } catch (NotExistException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/customers";
        }


    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("id") Long id
                     ) throws MessagingException, UnsupportedEncodingException {
        Customer customer = customerService.findById(id);
        customer.setUserState(UserState.CONFIRMED);
        sendConfirmEmail(customer);
        customerService.saveOrUpdate(customer);
        return "redirect:/customers";
    }


    private void sendConfirmEmail( Customer theCustomer) throws MessagingException, UnsupportedEncodingException {
        JavaMailSenderImpl mailSender = Utility.prepareMailSender();

        String toAddress = theCustomer.getEmail();
        String subject = "تایید نهایی ثبت نام";
        String content = "ثبت نام شما با موفقیت انجام شد میتوانید وارد سیستم شوید";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("vahidsaghlatoon66@gmail.com" , "Home_service");
        helper.setTo(toAddress);
        helper.setSubject(subject);

        helper.setText(content );
        mailSender.send(message);
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("customerId") Long theId, RedirectAttributes redirectAttributes) {
        Customer customer = customerService.deleteById(theId);
        redirectAttributes.addFlashAttribute("message",
                "مشتری با نام " + customer.getFirstName() + " " + customer.getLastName() + " حذف شد ");
        return "redirect:/customers";
    }

    @GetMapping("/{id}/enabled/{status}")
    public String updateEmployeeEnabledStatus(@PathVariable("id") Long id,
                                              @PathVariable("status") boolean enabled,
                                              RedirectAttributes redirectAttributes) {
        customerService.updateCustomerEnableStatus(id, enabled);
        String theStatus = enabled ? "فعال" : "غیرفعال";
        redirectAttributes.addFlashAttribute("message", "وضعیت مشتری به " + theStatus + " تغییر یافت");
        return "redirect:/customers";
    }
}

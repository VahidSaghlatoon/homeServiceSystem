package com.vahidsaghlatoon.homeservicesystem.controller;

import com.vahidsaghlatoon.homeservicesystem.model.EmployeeDetails;
import com.vahidsaghlatoon.homeservicesystem.model.Employee;
import com.vahidsaghlatoon.homeservicesystem.service.EmployeeService;
import com.vahidsaghlatoon.homeservicesystem.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final EmployeeService employeeService;

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal EmployeeDetails loggedUser , Model model){
        String email = loggedUser.getUsername();
        Employee employee = employeeService.findByEmail(email);
        model.addAttribute("employee",employee);
        return "/employees/account-form";
    }

    @PostMapping("/account/update")
    public String save(@Valid @ModelAttribute("employee") Employee theEmployee,
                       @AuthenticationPrincipal EmployeeDetails loggedUser,
                       BindingResult theBindingResult,
                       RedirectAttributes redirectAttributes,
                       @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (theBindingResult.hasErrors()) {
            return "/employees/account-form";
        }
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            theEmployee.setPhoto(fileName);
            Employee savedEmployee = employeeService.updateAccount(theEmployee);
            String uploadDir = "employee-photos/" + savedEmployee.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (theEmployee.getPhoto().isEmpty()) theEmployee.setPhoto(null);
            employeeService.updateAccount(theEmployee);
        }

        loggedUser.setFirstName(theEmployee.getFirstName());
        loggedUser.setLastName(theEmployee.getLastName());

        redirectAttributes.addFlashAttribute("message", "اطلاعات حساب بروزرسانی شد ");
        return "redirect:/account";
    }
}

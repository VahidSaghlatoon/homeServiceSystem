package com.vahidsaghlatoon.homeservicesystem.controller;

import com.vahidsaghlatoon.homeservicesystem.exception.NotExistException;
import com.vahidsaghlatoon.homeservicesystem.model.Employee;
import com.vahidsaghlatoon.homeservicesystem.model.Role;
import com.vahidsaghlatoon.homeservicesystem.service.EmployeeService;
import com.vahidsaghlatoon.homeservicesystem.service.RoleService;
import com.vahidsaghlatoon.homeservicesystem.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
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
        Page<Employee> page = employeeService.findAllByPage(pageNumber,sortField,sortDir,keyword);
        List<Employee> theEmployees = page.getContent();
        long startCount = (long) (pageNumber - 1) * EmployeeService.EMPLOYEE_PER_PAGE + 1;
        long endCount = startCount * EmployeeService.EMPLOYEE_PER_PAGE;
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
        theModel.addAttribute("theEmployees", theEmployees);
        theModel.addAttribute("sortField", sortField);
        theModel.addAttribute("sortDir", sortDir);
        theModel.addAttribute("reverseSortDir", reverseSortDir);
        return "/employees/list-employees";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        Employee theEmployee = new Employee();
        theEmployee.setEnabled(true);
        List<Role> theRoles = roleService.findAll();
        theModel.addAttribute("theRoles", theRoles);
        theModel.addAttribute("employee", theEmployee);
        return "/employees/employee-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("employee") Employee theEmployee,
                       BindingResult theBindingResult,
                       RedirectAttributes redirectAttributes,
                       @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (theBindingResult.hasErrors()) {
            return "/employees/employee-form";
        }
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            theEmployee.setPhoto(fileName);
            Employee savedEmployee = employeeService.saveOrUpdate(theEmployee);
            String uploadDir = "employee-photos/" + savedEmployee.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (theEmployee.getPhoto().isEmpty()) theEmployee.setPhoto(null);
            employeeService.saveOrUpdate(theEmployee);
        }
        redirectAttributes.addFlashAttribute("message", "کارمند جدید اضافه شد");
        return getRedirectUrlByAffectedEmployee(theEmployee);
    }

    private String getRedirectUrlByAffectedEmployee(Employee theEmployee) {
        String firstPartOfEmail = theEmployee.getEmail().split("@")[0];
        return "redirect:/employees/page/1?sortField=firstName&sortDir=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("employeeId") Long theId,
                                    Model theModel, RedirectAttributes redirectAttributes) {
        try {
            List<Role> theRoles = roleService.findAll();
            theModel.addAttribute("theRoles", theRoles);
            Employee theEmployee = employeeService.findById(theId);
            theModel.addAttribute("employee", theEmployee);
            return "/employees/employee-form";
        } catch (NotExistException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/employees";
        }


    }

    @GetMapping("/delete")
    public String delete(@RequestParam("employeeId") Long theId, RedirectAttributes redirectAttributes) {
        Employee employee = employeeService.deleteById(theId);
        redirectAttributes.addFlashAttribute("message",
                "کارمند با نام " + employee.getFirstName() + " " + employee.getLastName() + " حذف شد ");
        return "redirect:/employees";
    }

    @GetMapping("/{id}/enabled/{status}")
    public String updateEmployeeEnabledStatus(@PathVariable("id") Long id,
                                              @PathVariable("status") boolean enabled,
                                              RedirectAttributes redirectAttributes) {
        employeeService.updateEmployeeEnableStatus(id, enabled);
        String theStatus = enabled ? "فعال" : "غیرفعال";
        redirectAttributes.addFlashAttribute("message", "وضعیت کارمند به " + theStatus + " تغییر یافت");
        return "redirect:/employees";
    }
}

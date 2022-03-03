package com.vahidsaghlatoon.homeservicesystem.controller;

import com.vahidsaghlatoon.homeservicesystem.model.MainService;
import com.vahidsaghlatoon.homeservicesystem.model.Role;
import com.vahidsaghlatoon.homeservicesystem.service.RoleService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService ;

    @GetMapping
    public String findAll(Model theModel){
        List<Role> theRoles = roleService.findAll();
        theModel.addAttribute("theRoles" ,theRoles );
        return "/roles/list-roles";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        theModel.addAttribute("role", new Role());
        return "/roles/role-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("role") Role theRole,
                       BindingResult theBindingResult) {
        if (theBindingResult.hasErrors()) {
            return "/roles/role-form";
        }
        roleService.saveOrUpdate(theRole);
        return "redirect:/roles";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("roleId") Long theId,
                                    Model theModel) {
        Role theRole = roleService.findById(theId);
        theModel.addAttribute("role", theRole);
        return "/roles/role-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("roleId") Long theId) {
        roleService.deleteById(theId);
        return "redirect:/roles";
    }
}

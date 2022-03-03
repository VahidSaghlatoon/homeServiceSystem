package com.vahidsaghlatoon.homeservicesystem.controller;

import com.vahidsaghlatoon.homeservicesystem.model.MainService;
import com.vahidsaghlatoon.homeservicesystem.model.MainService;
import com.vahidsaghlatoon.homeservicesystem.service.MainServiceService;
import com.vahidsaghlatoon.homeservicesystem.service.MainServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/mainServices")
@RequiredArgsConstructor
@Slf4j
public class MainServiceController {
    private final MainServiceService mainServiceService;

    @GetMapping()
    public String findAll(Model theModel) {
        return findAllByPage(1, theModel,"title","asc",null);
    }
    @GetMapping("/page/{pageNumber}")
    public String findAllByPage(
            @PathVariable(name = "pageNumber") int pageNumber,
            Model theModel,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyword") String keyword
    ) {
        Page<MainService> page = mainServiceService.findAllByPage(pageNumber,sortField,sortDir,keyword);
        List<MainService> theMainServices = page.getContent();
        long startCount = (long) (pageNumber - 1) * MainServiceService.MAIN_SERVICE_PER_PAGE + 1;
        long endCount = startCount * MainServiceService.MAIN_SERVICE_PER_PAGE;
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
        theModel.addAttribute("theMainServices", theMainServices);
        theModel.addAttribute("sortField", sortField);
        theModel.addAttribute("sortDir", sortDir);
        theModel.addAttribute("reverseSortDir", reverseSortDir);
        return "/mainServices/main-services";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        MainService theMainService = new MainService();
        List<MainService> mainServiceList = mainServiceService.ListMainServiceForUseInForm();
        theModel.addAttribute("theMainService",theMainService);
        theModel.addAttribute("mainServiceList",mainServiceList);
        return "/mainServices/main-service-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("theMainService") MainService theMainService,
                       BindingResult theBindingResult,
                       RedirectAttributes redirectAttributes) {
        if (theBindingResult.hasErrors()) {
            return "/mainServices/main-service-form";
        }

        mainServiceService.saveOrUpdate(theMainService);
        redirectAttributes.addFlashAttribute("message", "خدمت جدید اضافه شد");

//        return "redirect:/mainServices/page/1?sortField=title&sortDir=asc&keyword=" + theMainService.getTitle();
        return "redirect:/mainServices";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("mainServiceId") Long theId,
                                    Model theModel) {
        MainService theMainService = mainServiceService.findById(theId);
        theModel.addAttribute("mainService", theMainService);
        return "/mainServices/main-service-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("mainServiceId") Long theId ,RedirectAttributes redirectAttributes) {
        mainServiceService.deleteById(theId);
        redirectAttributes.addFlashAttribute("message","خدمت حذف شد ");
        return "redirect:/mainServices";

    }
    @GetMapping("/{id}/enabled/{status}")
    public String updateMainServiceEnabledStatus(@PathVariable("id") Long id,
                                              @PathVariable("status") boolean enabled,
                                              RedirectAttributes redirectAttributes) {
        mainServiceService.updateMainServiceEnableStatus(id, enabled);
        String theStatus = enabled ? "فعال" : "غیرفعال";
        redirectAttributes.addFlashAttribute("message", "وضعیت خدمت به " + theStatus + " تغییر یافت");
        return "redirect:/mainServices";
    }


//    @PostMapping
//    public MainService save(@Valid @RequestBody MainService theMainService) {
//        return mainServiceService.save(theMainService);
//    }
//
//    @GetMapping
//    public List<MainService> findAll() {
//        return mainServiceService.findAll();
//    }
//
//    @GetMapping(value = "/{theId}")
//    public MainService findById(@PathVariable("theId") Long theId) {
//        return mainServiceService.findById(theId);
//    }
//
//    @DeleteMapping(value = "/{theId}")
//    public String deleteByID(@PathVariable("theId") Long theId) {
//        mainServiceService.deleteById(theId);
//        return theId.toString();
//    }
//    @PutMapping
//    public MainService update(@Valid @RequestBody MainService theMainService){
//        return mainServiceService.save(theMainService);
//    }
}

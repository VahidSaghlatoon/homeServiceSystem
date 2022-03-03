package com.vahidsaghlatoon.homeservicesystem.service;

import com.vahidsaghlatoon.homeservicesystem.exception.InvalidInputException;
import com.vahidsaghlatoon.homeservicesystem.exception.NotExistException;
import com.vahidsaghlatoon.homeservicesystem.model.MainService;
import com.vahidsaghlatoon.homeservicesystem.repository.MainServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MainServiceService {
    public static final int MAIN_SERVICE_PER_PAGE = 4;
    private final MainServiceRepository mainServiceRepository;

    @Transactional
    public MainService saveOrUpdate(MainService theMainService) {
        if (theMainService.getTitle().isBlank() || theMainService.getDescription().isBlank()
                || !theMainService.getTitle().matches("^[\u0600-\u06FF\uFB8A\u067E\u0686\u06AF\u200C\u200F ]+$")
                || !theMainService.getDescription().matches("^[\u0600-\u06FF\uFB8A\u067E\u0686\u06AF\u200C\u200F ]+$")
        ) {
            throw new InvalidInputException("input is wrong");
        }
        return mainServiceRepository.save(theMainService);
    }

    @Transactional
    public MainService findById(Long theId) {
        return mainServiceRepository.findById(theId)
                .orElseThrow(() -> new NotExistException("Did not find main service  by id - " + theId));
    }

    @Transactional
    public List<MainService> findAll() {
        return mainServiceRepository.findAllByOrderByTitleAsc();
    }

    @Transactional
    public void deleteById(Long theId) {
        mainServiceRepository.deleteById(theId);
    }

    @Transactional
    public boolean isTitleUnique(Long id, String theTitle) {
        MainService mainService = mainServiceRepository.findByTitle(theTitle.trim());
        if (mainService == null) {
            return true;
        }
        if (id != null) {
            return theTitle.equals(mainService.getTitle());
        }
        return false;
    }
    @Transactional
    public Page<MainService> findAll(Pageable pageable){
        return mainServiceRepository.findAll(pageable);
    }

    @Transactional
    public void updateMainServiceEnableStatus(Long id, boolean enabled) {
        mainServiceRepository.updateEnableStatus(id, enabled);
    }

    @Transactional
    public List<MainService> findRootMainServices(){
        return mainServiceRepository.findRootMainServices();
    }

    @Transactional
    public Page<MainService> findAllByPage(int pageNumber, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, MAIN_SERVICE_PER_PAGE, sort);
        if (keyword != null) {
            return mainServiceRepository.findAllByKeyword(keyword, pageable);
        }
        return mainServiceRepository.findAll(pageable);
    }

    @Transactional
    public MainService findByTitle(String title) {
        return mainServiceRepository.findByTitle(title);
    }
    @Transactional
    public List<MainService> ListMainServiceForUseInForm() {
        List<MainService> mainServiceUseInForm = new ArrayList<>();

        List<MainService> mainServiceList = mainServiceRepository.findAll();

        for (MainService mainService : mainServiceList) {
            if (mainService.getMainService() == null) {
                mainServiceUseInForm.add( copyIdAndName(mainService));
                Set<MainService> subServices = mainService.getSubServices();
                for (MainService subService : subServices) {
                    String name = "--" + subService.getTitle();
                    mainServiceUseInForm.add(copyIdAndName(subService.getId(),name));
                    listSubService(mainServiceUseInForm ,subService, 1);
                }
            }
        }
        return mainServiceUseInForm ;
    }
    @Transactional
    public void listSubService( List<MainService> mainServiceUseInForm ,MainService mainService, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<MainService> subServices = mainService.getSubServices();
        for (MainService subService : subServices) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name+= subService.getTitle();
            mainServiceUseInForm.add( copyIdAndName(subService.getId(),name));
            listSubService(mainServiceUseInForm ,subService, newSubLevel);
        }
    }
    public  MainService copyIdAndName(MainService mainService){

        return MainService.builder()
                .id(mainService.getId())
                .title(mainService.getTitle())
                .build();
    }

    public  MainService copyIdAndName(Long theId , String title){
        return MainService.builder()
                .id(theId)
                .title(title)
                .build();
    }
}

package com.vahidsaghlatoon.homeservicesystem.service;

import com.vahidsaghlatoon.homeservicesystem.exception.NotExistException;
import com.vahidsaghlatoon.homeservicesystem.model.Role;
import com.vahidsaghlatoon.homeservicesystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @Transactional
    public Role saveOrUpdate(Role theRole) {
        return roleRepository.save(theRole);
    }

    @Transactional
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional
    public Role findById(Long theId) {
        return roleRepository.findById(theId)
                .orElseThrow(() -> new NotExistException("Did not find main service  by id" + theId));
    }

    @Transactional
    public Role deleteById(Long theId) {
        Role theRole ;
        if ((theRole = findById(theId)) != null)
            roleRepository.deleteById(theId);
        return theRole ;
    }

}

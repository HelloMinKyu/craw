package com.service;

import com.entity.Citicommittee;
import com.entity.Free;
import com.repository.CiticommitteeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CiticommitteeService {
    @Autowired
    private CiticommitteeRepository citicommitteeRepository;

    @Transactional
    public Citicommittee save(Citicommittee citicommittee) {
        return citicommitteeRepository.save(citicommittee);
    }
}

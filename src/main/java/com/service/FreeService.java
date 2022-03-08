package com.service;

import com.entity.Free;
import com.entity.Notice;
import com.repository.FreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FreeService {
    @Autowired
    private FreeRepository freeRepository;

    @Transactional
    public Free save(Free free) {
        return freeRepository.save(free);
    }
}

package com.service;

import com.entity.Citicommittee;
import com.entity.Free;
import com.entity.Notice;
import com.repository.CiticommitteeRepository;
import command.SimpleSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    /* 중복 데이터 */
    public Citicommittee find(String writedate) {
        Citicommittee findwritedate = citicommitteeRepository.findByWritedate(writedate);
        return findwritedate;
    }

    public Page<Citicommittee> getPages(int page, int showNum, SimpleSearchRequest request) { //페이징처리

        if (page < 0) page = 0;

        PageRequest pageRequest = PageRequest.of(page, showNum, Sort.Direction.DESC, "id");

        String categoryStr = request.getCategory();

        Page<Citicommittee> pages = citicommitteeRepository.findAll(pageRequest);

        switch (categoryStr){
            case "name":
                pages = citicommitteeRepository.findAllByTitleLike(pageRequest,"%" + request.getValue() + "%");
                break;
        }

        return pages;
    }
}

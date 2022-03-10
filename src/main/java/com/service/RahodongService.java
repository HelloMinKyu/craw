package com.service;

import com.entity.Rahodong;
import com.repository.RahodongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RahodongService {
String type ="";
    @Autowired
    private RahodongRepository rahodongRepository;

    @Transactional
    public Rahodong save(Rahodong notice) {
        return rahodongRepository.save(notice);
    }

    /* 중복 데이터 */
    public Rahodong find(String writedate, String content) {
        Rahodong findWritedate = rahodongRepository.findByWritedateAndContent(writedate, content);
        return findWritedate;
    }

    public String findType(String url) {
        if(url.contains("01920")) {
            type="notice";
        }
        if(url.contains("05162")) {
            type="free";
        }
        if(url.contains("01917")) {
            type="city";
        }
        if(url.contains("01918")) {
            type="event";
        }
        return type;
    }

    @Transactional
    public Page<Rahodong> typeFind(int page, int showNum, String type) {
        PageRequest pageRequest = PageRequest.of(page, showNum, Sort.Direction.DESC, "id");
        return rahodongRepository.findAllByType(pageRequest, type);
    }


}

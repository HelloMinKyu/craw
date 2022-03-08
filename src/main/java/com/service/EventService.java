package com.service;

import com.entity.Event;
import com.entity.Free;
import com.repository.EventRepository;
import command.SimpleSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public Event save(Event evnet) {
        return eventRepository.save(evnet);
    }

    /* 중복 데이터 */
    public Event find(String writedate) {
        Event findwritedate = eventRepository.findByWritedate(writedate);
        return findwritedate;
    }

    @Transactional
    public Page<Event> getPages(int page, int showNum, SimpleSearchRequest request) { //페이징처리

        if (page < 0) page = 0;

        PageRequest pageRequest = PageRequest.of(page, showNum, Sort.Direction.DESC, "id");

        String categoryStr = request.getCategory();

        Page<Event> pages = eventRepository.findAll(pageRequest);

        switch (categoryStr){
            case "name":
                pages = eventRepository.findAllByTitleLike(pageRequest,"%" + request.getValue() + "%");
                break;
        }
        return pages;
    }

}

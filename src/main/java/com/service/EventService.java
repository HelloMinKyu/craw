package com.service;

import com.entity.Event;
import com.entity.Free;
import com.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

}

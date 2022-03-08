package com.service;

import com.entity.Event;
import com.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public Event save(Event evnet) {
        return eventRepository.save(evnet);
    }

}

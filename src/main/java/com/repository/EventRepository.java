package com.repository;

import com.entity.Event;
import com.entity.Free;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Event findByWritedate(String writedate);
    Page<Event> findAllByTitleLike(Pageable pageable, String title);
}

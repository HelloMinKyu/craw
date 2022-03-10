package com.repository;

import com.entity.Rahodong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RahodongRepository extends JpaRepository<Rahodong, Integer> {

    Rahodong findByWritedateAndContent(String Writedate, String content);

    List<Rahodong> findByType(String type);

    Page<Rahodong> findAllByType(Pageable pageable, String type);
}

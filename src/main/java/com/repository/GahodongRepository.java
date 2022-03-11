package com.repository;

import com.entity.Gahodong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GahodongRepository extends JpaRepository<Gahodong, Integer> {

    Gahodong findByWritedateAndContent(String Writedate, String content);

    Page<Gahodong> findAllByType(Pageable pageable, String type);
}

package com.repository;

import com.entity.Free;
import com.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeRepository extends JpaRepository<Free, Integer> {
    Page<Free> findAllByTitleLike(Pageable pageable, String title);
    Free findByWritedate(String writedate);
}

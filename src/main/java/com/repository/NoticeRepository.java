package com.repository;

import com.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    Page<Notice> findAllByTitleLike(Pageable pageable, String title);
}

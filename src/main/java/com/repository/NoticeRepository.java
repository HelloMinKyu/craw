package com.repository;

import com.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    Page<Notice> findAllByTitleLike(Pageable pageable, String title);
    Notice findByWritedate(String Writedate);
}

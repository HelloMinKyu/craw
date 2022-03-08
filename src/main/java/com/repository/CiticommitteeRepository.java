package com.repository;

import com.entity.Citicommittee;
import com.entity.Free;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CiticommitteeRepository extends JpaRepository<Citicommittee, Integer> {
    Citicommittee findByWritedate(String writedate);
    Page<Citicommittee> findAllByTitleLike(Pageable pageable, String title);

}

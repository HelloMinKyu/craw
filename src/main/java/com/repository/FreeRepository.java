package com.repository;

import com.entity.Free;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeRepository extends JpaRepository<Free, Integer> {
    Free findByWritedate(String writedate);
}

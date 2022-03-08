package com.service;

import com.entity.Notice;
import com.repository.NoticeRepository;
import command.SimpleSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Transactional
    public Notice save(Notice notice) {
        //validateDuplicateNoce(notice);
        return noticeRepository.save(notice);
    }

    /* 중복 데이터 */
    public Notice find(String writedate) {
        Notice findWritedate = noticeRepository.findByWritedate(writedate);
        return findWritedate;
    }

    @Transactional
    public Page<Notice> getPages(int page, int showNum, SimpleSearchRequest request) { //페이징처리

        if (page < 0) page = 0;

        PageRequest pageRequest = PageRequest.of(page, showNum, Sort.Direction.DESC, "id");

        String categoryStr = request.getCategory();

        Page<Notice> pages = noticeRepository.findAll(pageRequest);

        switch (categoryStr){
            case "name":
                pages = noticeRepository.findAllByTitleLike(pageRequest,"%" + request.getValue() + "%");
                break;
        }

        return pages;
    }

    public Page<Notice> boardList(Pageable pageable) { //게시글 리스트
        return noticeRepository.findAll(pageable);
    }

    @Modifying
    @Transactional
    public void deleteById(int id){ //게시글삭제
        noticeRepository.deleteById(id);
    }

    @Transactional
    public Notice getOne(int id){ //게시글보기
        return noticeRepository.getOne(id);
    }

    @Transactional
    public List<Notice> getList() {
        return noticeRepository.findAll();
    }
}

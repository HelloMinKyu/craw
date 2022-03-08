package com.controller;

import com.entity.Citicommittee;
import com.entity.Event;
import com.entity.Free;
import com.entity.Notice;
import com.handler.httpsSecurity;
import com.service.CiticommitteeService;
import com.service.EventService;
import com.service.FreeService;
import com.service.NoticeService;
import command.SimpleSearchRequest;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import util.PagingInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class HomeController {
    httpsSecurity httpsSecurity = new httpsSecurity();

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private FreeService freeService;

    @Autowired
    private CiticommitteeService citicommitteeService;

    @Autowired
    private EventService eventService;

    @RequestMapping(value = {"","/"})
    public String main() {
        return "home";
    }

    /* 가호동- 열린마당 - 공지사항 */
    @RequestMapping(value = "/crawling/notice")
    @ResponseBody
    public ResponseEntity<Object> noticeCrawling(HttpServletRequest request) {
        List<Notice> result = new ArrayList<>();
        httpsSecurity.getHttps();
        try {
            final int PAGE_INDEX = Integer.parseInt(request.getParameter("PAGE_INDEX"));
            final String url = "http://www.jinju.go.kr/00135/01114/01920.web?&cpage=" + PAGE_INDEX;
            Connection con = Jsoup.connect(url);

            /* 첫페이지에서 크롤링 해오는 목록 : 제목 */
            Document document = con.get();
            Elements titleElements = document.select("span.wrap1texts > strong.t1"); //title 경로
            Elements linkElements = document.select("div.wrap1 > a.a1");

            for(int j = 0; j < titleElements.size(); j++) {
                Notice notice = new Notice();
                final String title = titleElements.get(j).text(); //제목
                final String link = linkElements.get(j).attr("abs:href");

                /* 강의 링크 내부 */
                Connection innerCon = Jsoup.connect(link);
                Document innerDocument = innerCon.get();

                /* 시간 */
                Element dateElement = innerDocument.selectFirst("dd.dd1");
                final String writedate = dateElement.text();

                /* 작성자 */
                Element writeElement = innerDocument.selectFirst("dd[class='dd1 noprint']");
                final String write = writeElement.text();

                /* 본문내용 */
                Elements contentElement = innerDocument.select("div.substanceautolink");
                final String content = contentElement.text();

                if(noticeService.find(writedate) != null) {
                    System.out.println("중복있음");
                } else {
                    notice.setTitle(title);
                    notice.setLink(link);
                    notice.setWritedate(writedate);
                    notice.setWriter(write);
                    notice.setContent(content);
                    notice = noticeService.save(notice);
                    result.add(notice);
                }
            }
        } catch (Exception e) {
            System.out.println("오류 : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /* 가호동- 열린마당 - 자유게시판  */
    @RequestMapping(value = "/crawling/free")
    @ResponseBody
    public ResponseEntity<Object> freeCrawling(HttpServletRequest request) {
        List<Free> result = new ArrayList<>();
        httpsSecurity.getHttps();
        try {
            final int PAGE_INDEX = Integer.parseInt(request.getParameter("PAGE_INDEX"));
            final String url ="https://www.jinju.go.kr/00135/01114/05162.web?cpage=" + PAGE_INDEX;
            Connection con = Jsoup.connect(url);

            /* 첫페이지에서 크롤링 해오는 목록 : 제목, A링크 */
            Document document = con.get();
            Elements titleElements = document.select("span.wrap1texts > strong.t1"); // 제목 경로
            Elements linkElements = document.select("div.wrap1 > a.a1"); //a링크

            for(int j = 0; j < titleElements.size(); j++) {
                Free free = new Free();
                final String title = titleElements.get(j).text(); //제목
                final String link = linkElements.get(j).attr("abs:href");

                /* 강의 링크 내부 */
                Connection innerCon = Jsoup.connect(link);
                Document innerDocument = innerCon.get();

                /* 이미지 */
                Element imageElements = innerDocument.selectFirst("p.p1 > img"); //이미지경로
                String noImage = "이미지 없음";
                final String imageUrl = Objects.isNull(imageElements) ? noImage : imageElements.attr("abs:src");

                /* 시간 */
                Element writeDateElement = innerDocument.selectFirst("dd.dd1");
                final String writedate = writeDateElement.text();

                /* 작성자 */
                Element writerElement = innerDocument.selectFirst("dd[class='dd1 noprint']");
                final String writer = writerElement.text();

                /* 본문내용 */
                Elements contentElement = innerDocument.select("div.substanceautolink");
                final String content = contentElement.text();

                if(freeService.find(writedate) != null) {
                    System.out.println("중복있음");
                } else {
                    free.setTitle(title);
                    free.setLink(link);
                    free.setImagelink(imageUrl);
                    free.setWritedate(writedate);
                    free.setWriter(writer);
                    free.setContent(content);
                    free = freeService.save(free);
                    result.add(free);
                }
            }
        } catch (Exception e) {
            System.out.println("오류 : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);// 에러메세지500
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/crawling/city")
    @ResponseBody
    public ResponseEntity<Object> citicommitteeCrawling(HttpServletRequest request) {
        List<Citicommittee> result = new ArrayList<>();
        httpsSecurity.getHttps();
        try {
            final int PAGE_INDEX = Integer.parseInt(request.getParameter("PAGE_INDEX"));
            final String url ="https://www.jinju.go.kr/00135/01114/01917.web?cpage=" + PAGE_INDEX;
            Connection con = Jsoup.connect(url);

            /* 첫페이지에서 크롤링 해오는 목록 : 제목, 썸네일, A링크 */
            Document document = con.get();
            Elements titleElements = document.select("span.wrap1texts > strong.t1"); // 제목 경로
            Elements linkElements = document.select("div.wrap1 > a.a1"); //a링크

            for(int j = 0; j < titleElements.size(); j++) {
                Citicommittee citicommittee = new Citicommittee();
                final String title = titleElements.get(j).text(); //제목
                final String link = linkElements.get(j).attr("abs:href");

                /* 강의 링크 내부 */
                Connection innerCon = Jsoup.connect(link);
                Document innerDocument = innerCon.get();

                /* 이미지 */
                Element imageElements = innerDocument.selectFirst("p.p1 > img"); //이미지경로
                String noImage = "이미지 없음";
                final String imageUrl = Objects.isNull(imageElements) ? noImage : imageElements.attr("abs:src");

                /* 시간 */
                Element writeDateElement = innerDocument.selectFirst("dd.dd1");
                final String writedate = writeDateElement.text();

                /* 작성자 */
                Element writerElement = innerDocument.selectFirst("dd[class='dd1 noprint']");
                final String writer = writerElement.text();

                /* 본문내용 */
                Elements contentElement = innerDocument.select("div.substanceautolink");
                final String content = contentElement.text();

                if(citicommitteeService.find(writedate) != null) {
                    System.out.println("중복있음");
                } else {
                    citicommittee.setTitle(title);
                    citicommittee.setLink(link);
                    citicommittee.setImagelink(imageUrl);
                    citicommittee.setWritedate(writedate);
                    citicommittee.setWriter(writer);
                    citicommittee.setContent(content);
                    citicommittee = citicommitteeService.save(citicommittee);
                    result.add(citicommittee);
                }
            }
        } catch (Exception e) {
            System.out.println("오류 : " + e.getMessage());
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/crawling/event")
    @ResponseBody
    public ResponseEntity<Object> eventeeCrawling(HttpServletRequest request) {
        List<Event> result = new ArrayList<>();
        httpsSecurity.getHttps();
        try {
            final int PAGE_INDEX = Integer.parseInt(request.getParameter("PAGE_INDEX"));
            final String url ="https://www.jinju.go.kr/00135/01114/01918.web?cpage=" + PAGE_INDEX;
            Connection con = Jsoup.connect(url);

            /* 첫페이지에서 크롤링 해오는 목록 : 제목, 썸네일, A링크 */
            Document document = con.get();
            Elements titleElements = document.select("span.wrap1texts > strong.t1"); // 제목 경로
            Elements linkElements = document.select("div.wrap1 > a.a1"); //a링크

            for(int j = 0; j < titleElements.size(); j++) {
                Event event = new Event();
                final String title = titleElements.get(j).text(); //제목
                final String link = linkElements.get(j).attr("abs:href");

                /* 강의 링크 내부 */
                Connection innerCon = Jsoup.connect(link);
                Document innerDocument = innerCon.get();

                /* 이미지 */
                Element imageElements = innerDocument.selectFirst("p.p1 > img"); //이미지경로
                String noImage = "이미지 없음";
                final String imageUrl = Objects.isNull(imageElements) ? noImage : imageElements.attr("abs:src");

                /* 시간 */
                Element writeDateElement = innerDocument.selectFirst("dd.dd1");
                final String writedate = writeDateElement.text();

                /* 작성자 */
                Element writerElement = innerDocument.selectFirst("dd[class='dd1 noprint']");
                final String writer = writerElement.text();

                /* 본문내용 */
                Elements contentElement = innerDocument.select("div.substanceautolink");
                final String content = contentElement.text();

                if(eventService.find(writedate) != null) {
                    System.out.println("중복있음");
                } else {
                    event.setTitle(title);
                    event.setLink(link);
                    event.setImagelink(imageUrl);
                    event.setWritedate(writedate);
                    event.setWriter(writer);
                    event.setContent(content);
                    event = eventService.save(event);
                    result.add(event);
                }
            }
        } catch (Exception e) {
            System.out.println("오류 : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping("/list/{page}") //게시글리스트 페이지
    public String list(@PathVariable int page, @ModelAttribute("searchReq") SimpleSearchRequest searchReq, Model model){
        Page<Notice> pages = noticeService.getPages(page,10,searchReq);
        PagingInfo pagingInfo = new PagingInfo(pages);

        model.addAttribute("pagingInfo",pagingInfo);
        model.addAttribute("pathParam","?" + searchReq.toPathParam());
        return "list";
    }
}

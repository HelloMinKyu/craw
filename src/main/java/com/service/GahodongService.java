package com.service;

import com.entity.Gahodong;
import com.handler.httpsSecurity;
import com.repository.GahodongRepository;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class GahodongService {
    String type ="";
    httpsSecurity httpsSecurity = new httpsSecurity();

    @Autowired
    private GahodongRepository gahodongRepository;

    @Transactional
    public Gahodong save(Gahodong notice) {
        return gahodongRepository.save(notice);
    }

    /* 중복 데이터 */
    public Gahodong find(String writedate, String content) {
        Gahodong findWritedate = gahodongRepository.findByWritedateAndContent(writedate, content);
        return findWritedate;
    }

    public String findType(String url) {
        if(url.contains("01920")) {
            type="notice";
        }
        if(url.contains("05162")) {
            type="free";
        }
        if(url.contains("01917")) {
            type="city";
        }
        if(url.contains("01918")) {
            type="event";
        }
        return type;
    }

    /* 00시~23:59분 30분간격으로 실행 */
    @Scheduled(cron = "0 0/30 0-23 * * *")
    public boolean scheduledCrawl(){
        try {
            crawl("http://www.jinju.go.kr/00135/01114/01920.web?&cpage=1");
            crawl("https://www.jinju.go.kr/00135/01114/05162.web?cpage=1");
            crawl("https://www.jinju.go.kr/00135/01114/01918.web?cpage=1");
            crawl("https://www.jinju.go.kr/00135/01114/01917.web?cpage=1");
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
//    @Scheduled(fixedDelay = 1000)
//    public boolean scheduledCrawl(){
//        try {
//            crawl("http://www.jinju.go.kr/00135/01114/01920.web?&cpage=1");
//            crawl("https://www.jinju.go.kr/00135/01114/05162.web?cpage=1");
//            crawl("https://www.jinju.go.kr/00135/01114/01918.web?cpage=1");
//            crawl("https://www.jinju.go.kr/00135/01114/01917.web?cpage=1");
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }
    public List<Gahodong> crawl(String url){
        List<Gahodong> result = new ArrayList<>();
        try {
            final String type = findType(url);
            Connection con = Jsoup.connect(url);
            httpsSecurity.getHttps();
            /* 첫페이지에서 크롤링 해오는 목록 : 제목 */
            Document document = con.get();
            Elements titleElements = document.select("span.wrap1texts > strong.t1"); //title 경로
            Elements linkElements = document.select("div.wrap1 > a.a1");

            for(int j = 0; j < titleElements.size(); j++) {
                Gahodong rahodong = new Gahodong();
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

                /* 이미지 */
                Element imageElements = innerDocument.selectFirst("p.p1 > img"); //이미지경로
                String noImage = "";
                final String imageUrl = Objects.isNull(imageElements) ? noImage : imageElements.attr("abs:src");

                if(find(writedate, content) != null) {
                    System.out.println("이미 저장되어 있습니다.");
                } else {
                    rahodong.setTitle(title);
                    rahodong.setLink(link);
                    rahodong.setImagelink(imageUrl);
                    rahodong.setWritedate(writedate);
                    rahodong.setWriter(write);
                    rahodong.setContent(content);
                    rahodong.setType(type);
                    rahodong = save(rahodong);
                    result.add(rahodong);
                }
            }
        } catch (Exception e) {
            System.out.println("오류 : " + e.getMessage());
            return null;
        }

        return result;
    }

    @Transactional
    public Page<Gahodong> typeFind(int page, int showNum, String type) {
        PageRequest pageRequest = PageRequest.of(page, showNum, Sort.Direction.DESC, "id");
        return gahodongRepository.findAllByType(pageRequest, type);
    }


}

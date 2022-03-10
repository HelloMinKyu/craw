package com.controller;

import com.entity.Rahodong;
import com.handler.httpsSecurity;
import com.service.RahodongService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class HomeController {
    httpsSecurity httpsSecurity = new httpsSecurity();

    @Autowired
    private RahodongService rahodongService;


    @RequestMapping(value = {"","/"})
    public String main() {
        return "home";
    }

    @RequestMapping(value = "/crawling")
    @ResponseBody
    public ResponseEntity<Object> noticeCrawling(HttpServletRequest request) {
        List<Rahodong> result = new ArrayList<>();
        httpsSecurity.getHttps();
        try {
            final int PAGE_INDEX = Integer.parseInt(request.getParameter("PAGE_INDEX"));
            final String url = request.getParameter("address") + PAGE_INDEX;
            final String type = rahodongService.findType(url);
            Connection con = Jsoup.connect(url);

            /* 첫페이지에서 크롤링 해오는 목록 : 제목 */
            Document document = con.get();
            Elements titleElements = document.select("span.wrap1texts > strong.t1"); //title 경로
            Elements linkElements = document.select("div.wrap1 > a.a1");

            for(int j = 0; j < titleElements.size(); j++) {
                Rahodong rahodong = new Rahodong();
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

                if(rahodongService.find(writedate, content) != null) {
                    System.out.println("이미 저장되어 있습니다.");
                } else {
                    rahodong.setTitle(title);
                    rahodong.setLink(link);
                    rahodong.setImagelink(imageUrl);
                    rahodong.setWritedate(writedate);
                    rahodong.setWriter(write);
                    rahodong.setContent(content);
                    rahodong.setType(type);
                    rahodong = rahodongService.save(rahodong);
                    result.add(rahodong);
                }
            }
        } catch (Exception e) {
            System.out.println("오류 : " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

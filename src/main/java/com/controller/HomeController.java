package com.controller;

import com.entity.Notice;
import com.service.NoticeService;
import command.SimpleSearchRequest;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import util.PagingInfo;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@Controller
public class HomeController {

    @Autowired
    private NoticeService noticeService;

    @RequestMapping(value = {"","/"})
    public String main() {
        return "home";
    }

    @RequestMapping(value = "crawling")
    public String main(HttpServletRequest request) {
        try {
            final int PAGE_INDEX = Integer.parseInt(request.getParameter("PAGE_INDEX"));
            final String url = "http://www.jinju.go.kr/00135/01114/01920.web?&cpage=" + PAGE_INDEX;
            Connection con = Jsoup.connect(url);
            /* https 인증서 오류 */
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            /* 첫페이지에서 크롤링 해오는 목록 : 제목 */
            Document document = con.get();
            Elements titleElements = document.select("span.wrap1texts > strong.t1"); //title 경로
            Elements linkElements = document.select("div.wrap1 > a.a1");

            for(int j = 0; j < titleElements.size(); j++) {
                Notice notice = new Notice();
                final String title = titleElements.get(j).text(); //제목
                final String link = linkElements.get(j).attr("abs:href");
                System.out.println("제목 : " + title);
                System.out.println("링크 : " + link);

                /* 강의 링크 내부 */
                Connection innerCon = Jsoup.connect(link);
                Document innerDocument = innerCon.get();

                /* 시간 */
                Element dateElement = innerDocument.selectFirst("dd.dd1");
                final String writedate = dateElement.text();
                System.out.println("작성일 : " + writedate);

                /* 작성자 */
                Element writeElement = innerDocument.selectFirst("dd[class='dd1 noprint']");
                final String write = writeElement.text();
                System.out.println("작성자 : " +write);

                /* 본문내용 */
                Elements contentElement = innerDocument.select("div.substanceautolink");
                final String content = contentElement.text();
                System.out.println("내용 : " + content);
                System.out.println();

                notice.setTitle(title);
                notice.setLink(link);
                notice.setWritedate(writedate);
                notice.setWriter(write);
                notice.setContent(content);
                noticeService.save(notice);
            }
        } catch (Exception e) {
            System.out.println("오류 : " + e.getMessage());
        }
        return "home";
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

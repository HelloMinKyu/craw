package com.controller;

import com.entity.Citicommittee;
import com.entity.Event;
import com.entity.Free;
import com.entity.Notice;
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
import java.util.Objects;

@Controller
public class HomeController {

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
    @RequestMapping(value = "noticecrawling")
    public String noticeCrawling(HttpServletRequest request, Model model) {
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
        model.addAttribute("message", "정상적으로 저장되었습니다.");
        model.addAttribute("returnUrl", "/");
        return "redirect";
    }

    /* 가호동- 열린마당 - 자유게시판  */
    @RequestMapping(value = "freecrawling")
    public String freeCrawling(HttpServletRequest request, Model model) {
        final int PAGE_INDEX = Integer.parseInt(request.getParameter("PAGE_INDEX"));
        try {
            final String url ="https://www.jinju.go.kr/00135/01114/05162.web?cpage=" + PAGE_INDEX;
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

            /* 첫페이지에서 크롤링 해오는 목록 : 제목, 썸네일, A링크 */
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
                free.setTitle(title);
                free.setLink(link);
                free.setImagelink(imageUrl);
                free.setWritedate(writedate);
                free.setWriter(writer);
                free.setContent(content);
                freeService.save(free);
            }
        } catch (Exception e) {
            System.out.println("오류 : " + e.getMessage());
        }
        model.addAttribute("message", "정상적으로 저장되었습니다.");
        model.addAttribute("returnUrl", "/");
        return "redirect";
    }

    @RequestMapping(value = "citicommitteecrawling")
    public String citicommitteeCrawling(HttpServletRequest request, Model model) {
        final int PAGE_INDEX = Integer.parseInt(request.getParameter("PAGE_INDEX"));
        try {
            final String url ="https://www.jinju.go.kr/00135/01114/01917.web?cpage=" + PAGE_INDEX;
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

                citicommittee.setTitle(title);
                citicommittee.setLink(link);
                citicommittee.setImagelink(imageUrl);
                citicommittee.setWritedate(writedate);
                citicommittee.setWriter(writer);
                citicommittee.setContent(content);
                citicommitteeService.save(citicommittee);
            }
        } catch (Exception e) {
            System.out.println("오류 : " + e.getMessage());
        }
        model.addAttribute("message", "정상적으로 저장되었습니다.");
        model.addAttribute("returnUrl", "/");
        return "redirect";
    }

    @RequestMapping(value = "eventeecrawling")
    public String eventeeCrawling(HttpServletRequest request, Model model) {
        final int PAGE_INDEX = Integer.parseInt(request.getParameter("PAGE_INDEX"));
        try {
            final String url ="https://www.jinju.go.kr/00135/01114/01918.web?cpage=" + PAGE_INDEX;
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
                event.setTitle(title);
                event.setLink(link);
                event.setImagelink(imageUrl);
                event.setWritedate(writedate);
                event.setWriter(writer);
                event.setContent(content);
                eventService.save(event);
            }
        } catch (Exception e) {
            System.out.println("오류 : " + e.getMessage());
        }
        model.addAttribute("message", "정상적으로 저장되었습니다.");
        model.addAttribute("returnUrl", "/");
        return "redirect";
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

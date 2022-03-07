package main;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class main {

    public static void main(String args[]) throws KeyManagementException, NoSuchAlgorithmException {
        final String url ="http://www.jinju.go.kr/00135/01114/01920.web?&cpage=1";
        Connection con = Jsoup.connect(url);
        /*
        PKIX path building faile 인증서 오류 해결을 위한 코드
        */
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

        try {
            /*
            첫페이지에서 크롤링 해오는 목록 : title, 작성일, 작성자
             */
            Document document = con.get();
            Elements titleElements = document.select("span.wrap1texts > strong.t1"); //title 경로
            Elements dateElements = document.select("i.wrap1t3 > span.t3"); //작성일

            for(int j = 0; j < titleElements.size(); j++) {
                final String title = titleElements.get(j).text();
                //final String date = getT3(date);
                final String date = dateElements.get(j).text();
                System.out.println("제목 : " + title);
                System.out.println("작성일 : " + date);
            }

        } catch(IOException e) {
            System.out.println("오류 : " + e.getMessage());
        }
    }

    /*
    i.wrap1t3 > span.t3 에서 t3이 3개가 있어서 첫번째 인덱스만 뽑아오기 위해
     */
    private static String getT3(final String date) {
        final String[] dateArray = date.split("");
        return dateArray[0];
    }
}

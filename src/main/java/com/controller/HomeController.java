package com.controller;

import com.entity.Gahodong;
import com.handler.httpsSecurity;
import com.service.GahodongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class HomeController {
    httpsSecurity httpsSecurity = new httpsSecurity();

    @Autowired
    private GahodongService gahodongService;


    @RequestMapping(value = {"","/"})
    public String main() {
        return "home";
    }

    @RequestMapping(value = "/crawling")
    @ResponseBody
    public ResponseEntity<Object> noticeCrawling(HttpServletRequest request) {
        final int PAGE_INDEX = Integer.parseInt(request.getParameter("PAGE_INDEX"));
        final String url = request.getParameter("address") + PAGE_INDEX;

        List<Gahodong> result = gahodongService.crawl(url);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

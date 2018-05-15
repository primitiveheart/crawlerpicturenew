package com.controller;

import com.algorithm.BaiduSearchResultCrawler;
import com.alibaba.fastjson.JSONObject;
import com.entity.Crawler;
import com.entity.Keyword;
import com.entity.Url;
import com.mapper.CrawlerMapper;
import com.mapper.KeywordMapper;
import com.mapper.UrlMapper;
import com.sun.media.sound.UlawCodec;
import com.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 2018/5/6.
 */
@Controller
public class HomeController {

    @Autowired
    private BaiduSearchResultCrawler baiduSearchResultCrawler;

    @Autowired
    private KeywordMapper keywordMapper;

    @Autowired
    private UrlMapper urlMapper;

    @Autowired
    private CrawlerMapper crawlerMapper;

    @RequestMapping("home.html")
    public String home(HttpServletRequest request){
        try{
            List<Keyword> keywords = keywordMapper.listKeyword();
            List<Url> urls = urlMapper.listUrl();

//            //处理只有关键的情况
//            List<Crawler> crawlers = crawlerMapper.listAllCrawler();

            request.setAttribute("keywords", keywords);
            request.setAttribute("urls", urls);
//            request.setAttribute("crawlers", crawlers);

        }catch (Exception e){
            e.printStackTrace();
        }
        return "home";
    }

    @RequestMapping("getSearchKeyword.html")
    @ResponseBody
    public void getSearchKeyword(HttpServletResponse response){
        JSONObject jsonObject = new JSONObject();
        //来自关键字表的关键字
        List<Keyword> keywords = keywordMapper.getKeywordByIsSearch("1");
        //来自于网址的关键字
        List<Url> urls = urlMapper.getKeywordByIsSearch("1");
        List<String> keywordString = getKeywordStringFromUrlAndKeyword(keywords, urls);

        List<Crawler> crawlers = crawlerMapper.getPageByCommonSearchKeywordId(1,10,keywords.get(0).getId(), -1);

        jsonObject.put("keywordString", keywordString);
        jsonObject.put("crawlers", crawlers);
        jsonObject.put("test", "test");

        ResponseUtils.renderJson(response, jsonObject);
    }

    @RequestMapping("getAlreadyCrawlerKeyword.html")
    @ResponseBody
    public void getAlreadyCrawlerKeyword(HttpServletResponse response){
        JSONObject jsonObject = new JSONObject();
        //来自关键字表的关键字
        List<Keyword> keywords = keywordMapper.getKeywordByIsNew("0");
        //来自于网址的关键字
        List<Url> urls = urlMapper.getUrlByIsNew("0");
        List<String> crawlerKeywordString = getKeywordStringFromUrlAndKeyword(keywords, urls);

        jsonObject.put("crawlerKeywordString", crawlerKeywordString);

        ResponseUtils.renderJson(response, jsonObject);

    }

    /**
     * 从url表和keyword表中的关键字的字符串
     * @param keywords
     * @param urls
     * @return
     */
    public List<String> getKeywordStringFromUrlAndKeyword(List<Keyword> keywords, List<Url> urls) {
        List<String> keywordString = new ArrayList<String>();
        List<String> kKeyword = new ArrayList<String>();
        List<String> uKeyword = new ArrayList<String>();
        List<String> commonKeyword = new ArrayList<String>();
        for(int i=0; i< keywords.size(); i++){
            kKeyword.add(keywords.get(i).getKeyword());
        }
        for(int i=0 ;i < urls.size(); i++){
            //添加了_进行标识
            uKeyword.add(urls.get(i).getKeyword());
        }

        for(int i=0; i < kKeyword.size(); i++){
            if(uKeyword.contains(kKeyword.get(i))){
                keywordString.add(kKeyword.get(i) + "_common");
                commonKeyword.add(kKeyword.get(i));
            }else{
                keywordString.add(kKeyword.get(i));
            }
        }

        for(int i=0; i < uKeyword.size(); i++){
            if(!commonKeyword.contains(uKeyword.get(i))){
                keywordString.add(uKeyword.get(i) + "_webSite");
            }
        }
        return keywordString;
    }

    @RequestMapping("getPageBySearchKeyword.html")
    @ResponseBody
    public void getPageBySearchKeyword(HttpServletResponse response, Integer currentPage, Integer pageSize, String searchKeyword){
        JSONObject jsonObject = new JSONObject();
        Integer startIndex = (currentPage - 1) * pageSize;
        Long totalCounts = 0L;
        List<Crawler> crawlers = new ArrayList<Crawler>();
        try{
            //说明该关键字是某个网站的关键字
            if(searchKeyword.contains("_")){
                Url url = urlMapper.getUrlByKeyword(searchKeyword.split("_")[0]);
                //共同的关键字
                if(searchKeyword.contains("common")){
                    Keyword keyword = keywordMapper.getKeywordBykeyword(searchKeyword.split("_")[0]);
                    totalCounts = crawlerMapper.getCrawlerTotalByCommonSearchKeywordId(keyword.getId(), url.getId());
                    crawlers = crawlerMapper.getPageByCommonSearchKeywordId(startIndex, pageSize, keyword.getId(), url.getId());
                }else{
                    totalCounts = crawlerMapper.getCrawlerTotalBySearchKeywordId(-1, url.getId());
                    crawlers = crawlerMapper.getPageBySearchKeywordId(startIndex, pageSize, -1, url.getId());
                }
            }else{
                Keyword keyword = keywordMapper.getKeywordBykeyword(searchKeyword);
                totalCounts = crawlerMapper.getCrawlerTotalBySearchKeywordId(keyword.getId(), -1);
                crawlers = crawlerMapper.getPageBySearchKeywordId(startIndex, pageSize, keyword.getId(), -1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        jsonObject.put("pageCount", (totalCounts / pageSize) + 1);
        jsonObject.put("crawlers", crawlers);

        ResponseUtils.renderJson(response, jsonObject);
    }

    /**
     * @param already
     * @param newAdd
     */
    @RequestMapping("submitKeyword.html")
    @ResponseBody
    @Transactional
    public void submitKeyword(String already, String newAdd){
        try{
            //第一步:查询出来isSearch=1
            List<Keyword> k = keywordMapper.getKeywordByIsSearch("1");

           if(k != null && k.size() > 0){
               //第二步:更新isSearch=0
               for(int i=0 ;i < k.size(); i++){
                   k.get(i).setIsSearch("0");
               }
               keywordMapper.batchUpdateKeyword(k);
           }
            //对于已经存在的关键字
           if(already != ""){
               //第一步：对关键字进行查询
               List<Keyword> keywords = new ArrayList<Keyword>();
               if(already.contains(",")){//多个关键字
                   List<String> alreadyList = Arrays.asList(already.split(","));
                   for(int i=0 ; i < alreadyList.size(); i++){
                       Keyword keyword = keywordMapper.getKeywordBykeyword(alreadyList.get(i));
                       keyword.setIsSearch("1");
                       keyword.setIsNew("0");
                       keywords.add(keyword);
                   }
               }else{//一个关键字
                   Keyword keyword = keywordMapper.getKeywordBykeyword(already);
                   keyword.setIsSearch("1");
                   keyword.setIsNew("0");
                   keywords.add(keyword);
               }
               if(keywords.size() > 0){
                   //第二步: 对关键字进行修改,进行批量的更新
                   keywordMapper.batchUpdateKeyword(keywords);
               }

           }
            //对于新增的关键字进行插入
            //先进行查询看是否存在
            if(newAdd != ""){
                List<Keyword> newAddKeyword = new ArrayList<Keyword>();
                if(newAdd.contains(",")){//多个关键字
                    List<String> newAddList = Arrays.asList(newAdd.split(","));
                    for(int i=0; i<newAddList.size(); i++){
                        Keyword queryKeyword = keywordMapper.getKeywordBykeyword(newAddList.get(i));
                        if(queryKeyword == null) {
                            Keyword keyword = new Keyword();
                            keyword.setIsSearch("1");
                            keyword.setIsNew("1");
                            keyword.setKeyword(newAddList.get(i));
                            newAddKeyword.add(keyword);
                        }
                    }
                }else {//一个关键字
                    Keyword queryKeyword = keywordMapper.getKeywordBykeyword(newAdd);
                    if(queryKeyword == null){
                        Keyword keyword = new Keyword();
                        keyword.setIsSearch("1");
                        keyword.setIsNew("1");
                        keyword.setKeyword(newAdd);
                        newAddKeyword.add(keyword);
                    }
                }
               if(newAddKeyword.size() > 0){
                   keywordMapper.batchInsertkeyword(newAddKeyword);
               }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("submitUrl.html")
    @ResponseBody
    @Transactional
    public void submitUrl(String alreadyUrl,String alreayUrlNewKeyword, String alreadyUrlKeyword, String newUrl, String newUrlKeyword){
        try{
            //第一步:先查询isSearch = 1的Url
            List<Url> urls = urlMapper.getKeywordByIsSearch("1");

           if(urls != null && urls.size() > 0){
               //第二步:进行更新isSearch = 0
               for(int i=0; i<urls.size(); i++){
                   urls.get(i).setIsSearch("0");
               }
               urlMapper.batchUpdateUrlByUrlSite(urls);
           }
            //对于已经存在的网址
            if(alreadyUrl != ""){
                //第一步: 对于已经存在的网址，进行更新
                if(alreadyUrlKeyword != ""){
                    List<Url> alreadyKeywordList = new ArrayList<Url>();
                    List<String> keywords = new ArrayList<String>();
                    if(alreadyUrlKeyword.contains(",")){
                        keywords = Arrays.asList(alreadyUrlKeyword.split(","));
                        for(int i=0; i<keywords.size(); i++){
                            Url url = urlMapper.getUrlByUrlSiteAndKeyword(alreadyUrl,keywords.get(i));
                            if(url != null){
                                url.setIsSearch("1");
                                url.setIsNew("0");
                                alreadyKeywordList.add(url);
                            }
                        }
                    }else{
                        Url url = urlMapper.getUrlByUrlSiteAndKeyword(alreadyUrl,alreadyUrlKeyword);
                        if(url != null){
                            url.setIsSearch("1");
                            url.setIsNew("0");
                            alreadyKeywordList.add(url);
                        }
                    }
                   if(alreadyKeywordList.size() > 0){
                       //进行批量的更新
                       urlMapper.batchUpdateUrlByUrlSite(alreadyKeywordList);
                   }
                }


                //第二步:对于已经存在的网址，进行添加新的关键字
               if(alreayUrlNewKeyword != ""){
                   List<Url> newUrlAndKeyword = new ArrayList<Url>();
                   if(alreayUrlNewKeyword.contains(",")){
                       List<String> alreayUrlNewKeywordList = Arrays.asList(alreayUrlNewKeyword.split(","));
                       for(int i =0; i < alreayUrlNewKeywordList.size(); i++){
                           Url queryUrl = urlMapper.getUrlByKeyword(alreayUrlNewKeywordList.get(i));
                           if(queryUrl == null){
                               Url url = new Url();
                               url.setIsNew("1");
                               url.setIsSearch("1");
                               url.setKeyword(alreayUrlNewKeywordList.get(i));
                               url.setUrlSite(alreadyUrl);
                               newUrlAndKeyword.add(url);
                           }
                       }

                   }else {
                       Url queryUrl = urlMapper.getUrlByKeyword(alreadyUrlKeyword);
                      if(queryUrl == null){
                          Url url = new Url();
                          url.setIsNew("1");
                          url.setIsSearch("1");
                          url.setKeyword(alreayUrlNewKeyword);
                          url.setUrlSite(alreadyUrl);
                          newUrlAndKeyword.add(url);
                      }
                   }
                   if(newUrlAndKeyword.size() > 0){
                       //进行批量插入
                       urlMapper.batchInsertUrl(newUrlAndKeyword);
                   }
               }
            }

            //对于新增的网址
            //进行网址的插入和关键字的插入
            List<Url> queryUrl = urlMapper.getKeywordByUrlSite(newUrl);
            if(queryUrl.size() == 0){
                List<Url> newUrlList = new ArrayList<Url>();
                if(newUrl != "" && newUrlKeyword != ""){
                    if(newUrlKeyword.contains(",")){
                        List<String> middleKeyword = Arrays.asList(newUrlKeyword.split(","));
                        for(int i=0; i < middleKeyword.size(); i++){
                            Url url = new Url();
                            url.setIsNew("1");
                            url.setIsSearch("1");
                            url.setUrlSite(newUrl);
                            url.setKeyword(middleKeyword.get(i));
                            newUrlList.add(url);
                        }
                    }else {
                        Url url = new Url();
                        url.setIsNew("1");
                        url.setIsSearch("1");
                        url.setUrlSite(newUrl);
                        url.setKeyword(newUrlKeyword);
                        newUrlList.add(url);
                    }
                }

                if(newUrlList.size() > 0){
                    //进行批量插入
                    urlMapper.batchInsertUrl(newUrlList);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("getKeywordByUrlSite.html")
    @ResponseBody
    public void getKeywordByUrlSite(HttpServletResponse response, String urlSite){
        JSONObject jsonObject = new JSONObject();
        try{
            List<Url> urlKeywords = urlMapper.getKeywordByUrlSite(urlSite);
            jsonObject.put("urlKeywords", urlKeywords);
        }catch (Exception e){
            e.printStackTrace();
        }

        ResponseUtils.renderJson(response, jsonObject);
    }
}

package com.algorithm;

import com.entity.Crawler;
import com.mapper.CrawlerMapper;
import com.mapper.UrlMapper;
import com.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2018/5/2.
 */
@Component
public class WebCrawler {

    @Autowired
    private CrawlerMapper crawlerMapper;

    private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";


    /**
     * 示例："http://blog.sina.com.cn/blog"
     * @param url
     * @param keyword
     */
    public void insertWebSearchResult(String url, String keyword, Integer id) {
        //得到某个网站的所有的url
        List<String> urls = crawlerAllLinks(url);
        List<Crawler> crawlers = new ArrayList<Crawler>();
        for(int i=0; i < urls.size(); i++){
            try{
                List<Crawler> tempCramlers = Utils.getHtmlPicRelateContentByURL(urls.get(i), keyword, id);
                crawlers.addAll(tempCramlers);
                //批量进行插入
                if(i % 10 == 0){
                    crawlerMapper.batchInsertCrawler(crawlers);
                    crawlers.clear();
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                crawlers.clear();
            }
        }
    }


//    public void insertWebSearchResult(String url, String keyword, Integer id) {
//        //得到某个网站的所有的url
//        List<String> urls = getWebAllURL(url);
//        List<Crawler> crawlers = new ArrayList<Crawler>();
//        for(int i=0; i < urls.size(); i++){
//            try{
//                List<Crawler> tempCramlers = Utils.getHtmlPicRelateContentByURL(urls.get(i), keyword, id);
//                crawlers.addAll(tempCramlers);
//                //批量进行插入
//                if(i % 10 == 0){
//                    crawlerMapper.batchInsertCrawler(crawlers);
//                    crawlers.clear();
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }finally {
//                crawlers.clear();
//            }
//        }
//    }

    public List<String> crawlerAllLinks(String baseUrl){
        List<String> allLinks = new ArrayList<String>();

        String oldLinkHost = "";

        Pattern p = Pattern.compile("(https?://)?[^/\\s]*");
        Matcher matcher = p.matcher(baseUrl);
        if(matcher.find()){
            oldLinkHost = matcher.group();
        }
        allLinks = getOneWebSiteAllLinks(baseUrl, oldLinkHost);
        return allLinks;
    }

//    public  List<String> getWebAllURL(String baseUrl){
//        //存储是否遍历的键值对
//        Map<String, Boolean> oldMap = new LinkedHashMap<String, Boolean>();
//
//        String oldLinkHost = "";
//
//        Pattern p = Pattern.compile("(https?://)?[^/\\s]*");
//        Matcher matcher = p.matcher(baseUrl);
//        if(matcher.find()){
//            oldLinkHost = matcher.group();
//        }
//        oldMap.put(baseUrl, false);
//        oldMap = crawlLinks(oldLinkHost, oldMap);
//
//        return new ArrayList<String>(oldMap.keySet());
//    }

    public List<String> getOneWebSiteAllLinks(String baseUrl, String oldLinkHost){
        List<String> allLinks = new ArrayList<String>();
        BufferedReader reader = null;
        HttpURLConnection connection = null;
        //发起get请求
        try{
            connection = Utils.getHttpURLConnection(baseUrl);
            String charset = Utils.getCharset(connection);
            if(connection.getResponseCode() == 200){
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
                String line = "";
                Pattern pattern = Pattern.compile("<a.*?href=[\"']?((https?://)?/?[^\"']+)[\"']?.*?>(.+)</a>");
//                Pattern pattern = Pattern.compile("<a.*?href=((https?://)?/?[^\"']+)[\"']?.*?>(.+)</a>");
//                Pattern pattern = Pattern.compile("href=(https?://).*");
                Matcher matcher = null;
                while ((line = reader.readLine()) != null){
                    matcher = pattern.matcher(line);
                    if(matcher.find()){
                        String newLink = matcher.group(1).trim();
//                        String newLink = matcher.group(1).trim();
//                        if(!newLink.startsWith("http")){
//                            if(newLink.startsWith("/")){
//                                newLink = oldLinkHost + newLink;
//                            }else{
//                                newLink = oldLinkHost + "/" + newLink;
//                            }
//                        }
//                        //去除末尾的/
//                        if(newLink.endsWith("/")){
//                            newLink = newLink.substring(0, newLink.length() - 1);
//                        }

                        //去重，并丢弃其他网站的链接
//                        if(!allLinks.contains(newLink) && newLink.startsWith(oldLinkHost)){

//                        }
                        if(newLink.startsWith("http")){
                            allLinks.add(newLink);
                        }
                    }
                }
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader != null){
                try {
                    reader.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            connection.disconnect();
        }

        return allLinks;
    }



//    public Map<String, Boolean> crawlLinks(String oldLinkHost, Map<String, Boolean> oldMap){
//        Map<String, Boolean> newMap = new LinkedHashMap<String, Boolean>();
//        String oldLink = "";
//        for(Map.Entry<String, Boolean> entry : oldMap.entrySet()){
//            System.out.println("link:" + entry.getKey() + "--------check:"
//                    + entry.getValue());
//            //如果没有遍历的进行url发起get请求
//            if(!entry.getValue()){
//                oldLink = entry.getKey();
//                BufferedReader reader = null;
//                HttpURLConnection connection = null;
//                //发起get请求
//                try{
//                     connection = Utils.getHttpURLConnection(oldLink);
//
//                    if(connection.getResponseCode() == 200){
//                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
//                        String line = "";
//                        Pattern pattern = Pattern.compile("<a.*?href=[\"']?((https?://)?/?[^\"']+)[\"']?.*?>(.+)</a>");
//                        Matcher matcher = null;
//                        while ((line = reader.readLine()) != null){
//                            matcher = pattern.matcher(line);
//                            if(matcher.find()){
//                                String newLink = matcher.group(1).trim();
//                                if(!newLink.startsWith("http")){
//                                    if(newLink.startsWith("/")){
//                                        newLink = oldLinkHost + newLink;
//                                    }else{
//                                        newLink = oldLinkHost + "/" + newLink;
//                                    }
//                                }
//                                //去除末尾的/
//                                if(newLink.endsWith("/")){
//                                    newLink = newLink.substring(0, newLink.length() - 1);
//                                }
//
//                                //去重，并丢弃其他网站的链接
//                                if(!oldMap.containsKey(newLink) && !newMap.containsKey(newLink) && newLink.startsWith(oldLinkHost)){
//                                    newMap.put(newLink, false);
//                                }
//                            }
//                        }
//                    }
//                }catch (MalformedURLException e){
//                    e.printStackTrace();
//                }catch (IOException e) {
//                    e.printStackTrace();
//                }finally {
//                    if(reader != null){
//                        try {
//                            reader.close();
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                    connection.disconnect();
//
//                }
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                oldMap.put(oldLink, true);
//            }
//        }

//       try{
//           //有新的链接，继续遍历
//           if(!newMap.isEmpty()){
//               oldMap.putAll(newMap);
//               oldMap.putAll(crawlLinks(oldLinkHost, oldMap));
//           }
//       }catch (Exception e){
//           e.printStackTrace();
//       }
//
//        return oldMap;
//    }


}



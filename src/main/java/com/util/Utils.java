package com.util;


import com.entity.Crawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2018/4/27.
 */
public class Utils {

    //得到的是connection
    public static HttpURLConnection getHttpURLConnection(String url) throws IOException {
        URL realUrl = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(2000);
        connection.setReadTimeout(2000);
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        return connection;
    }

    public static String getCharset(HttpURLConnection connection){
        String contentType = connection.getContentType();
        Pattern pattern = Pattern.compile("charset=.*");
        Matcher matcher = pattern.matcher(contentType);
        if(matcher.find()){
            return matcher.group(0).split("charset=")[1];
        }else{
            return "utf-8";
        }

    }




    /**
     * 根据url得到image的url的
     * @param url
     * @param keyword 当关键字为空时，为百度搜索结果，当关键字不为空时，为对某个网站的搜索
     * @return
     * @throws Exception
     */
    public static List<Crawler> getHtmlPicRelateContentByURL(String url, String keyword, Integer id) throws Exception{
        List<Crawler> crawlers = new ArrayList<>();
        String html = Utils.getHtml(url);
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("div > img");
        for(Element element : elements){

            String alt = element.attr("alt");
            String src = element.attr("src");

            if(src.startsWith("//")){
                src = url.split(":")[0] + src;
            }

            if(src.startsWith("http")){
                String description = getPictureContextDescription(element);
                Crawler crawler = new Crawler();
                //这是百度搜索的结果
                if(keyword == ""){

                    crawler.setUrlId(-1);
                    crawler.setKeywordId(id);
                    crawler.setPictureURL(src);
                    crawler.setWebURL(url);
                    crawler.setPictureName(alt);
                    crawler.setPictureDescription(description);

                }else {
                    //这是对某个网站搜索的结果
                    if(alt.contains(keyword)){
                        crawler.setUrlId(id);
                        crawler.setKeywordId(-1);
                        crawler.setPictureURL(src);
                        crawler.setWebURL(url);
                        crawler.setPictureName(alt);
                        crawler.setPictureDescription(description);
                    }
                }
                crawlers.add(crawler);
            }


        }
        return crawlers;
    }

    public static String getPictureContextDescription(Element element) {
        String description = "";
        Element parent = element.parent();
        if(parent != null){
            Element pre = parent.previousElementSibling();
            if(pre != null && pre.hasText()){
                if(pre.text().length() > 20){
                    description = pre.text();
                }else{
                    pre = pre.previousElementSibling();
                    if(pre != null && pre.hasText()){
                        description = pre.text();
                    }
                }
            }else{
                Element next = parent.nextElementSibling();
                if(next != null && next.hasText()){
                    if(next.text().length() > 20){
                        description = next.text();
                    }else{
                        next = next.nextElementSibling();
                        if(next != null && next.hasText()){
                            description = next.text();
                        }
                    }
                }
            }
        }
        return description;
    }


    //得到重定向的url
    public static String getURLByRedirect(String realUrl) {
        String redirectAfterUrl = "";
        HttpURLConnection connection = null;
        try {
            connection = Utils.getHttpURLConnection(realUrl);
            //设置不允许重定向
            connection.setInstanceFollowRedirects(false);
            if(connection.getResponseCode() == 302){
                redirectAfterUrl = connection.getHeaderField("Location");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }
        return redirectAfterUrl;
    }

    //得到html
    public static String getHtml(String url){

        HttpURLConnection connection = null;

        StringBuffer sb = new StringBuffer();
        BufferedReader reader = null;

       try{
           connection = Utils.getHttpURLConnection(url);
           String charset = getCharset(connection);
           if(connection.getResponseCode() == 200){
               reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
               String line;

               while ((line = reader.readLine()) != null) {
                   sb.append(line,0,line.length());
                   sb.append('\n');
               }

           }
       }catch (Exception e){
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

        return sb.toString();
    }

}

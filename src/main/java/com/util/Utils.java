package com.util;


import com.constants.Constant;
import com.entity.Crawler;
import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.document.Image;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import de.l3s.boilerpipe.sax.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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

        String referer = realUrl.getProtocol() + "://" + realUrl.getHost();
        connection.setRequestProperty("Host", realUrl.getHost());
        connection.setRequestProperty("Referer", referer);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        return connection;
    }

    public static String getCharset(HttpURLConnection connection) throws IOException{
//        String contentType = connection.getContentType();
//        Pattern pattern = Pattern.compile("charset=.*");
//        Matcher matcher = pattern.matcher(contentType);
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {
            if(line.contains("charset")){
                Pattern pattern = Pattern.compile("charset=.*");
                Matcher matcher = pattern.matcher(line);

                if(matcher.find()){
                    String temp = matcher.group(0);
                    if(temp.contains("/>")){
                        return temp.replaceAll("/>", "").split("=")[1].replaceAll("\"","").trim();
                    }else{
                        return temp.split("=")[1].replaceAll("\"","").trim();
                    }
                }else{
                    return "utf-8";
                }
            }
        }

        return "utf-8";
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
        Integer bodyFrequence = getHtmlKeyWordNumber(html, keyword);
        Integer titleFrequence = getTitleKeywordNumber(html, keyword);
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("p > img");
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
                    crawler.setPictureSource(Constant.BAIDUSEARCH);

                }else {
                    //这是对某个网站搜索的结果
                    if(alt.contains(keyword)){
                        crawler.setUrlId(id);
                        crawler.setKeywordId(-1);
                        crawler.setPictureSource(Constant.SOMEWEB);
                    }
                }
                crawler.setPictureURL(src);
                crawler.setWebURL(url);
                crawler.setPictureName(alt);
                crawler.setPictureDescription(description);
                crawler.setBodyFrequence(bodyFrequence);
                crawler.setTitleFrequence(titleFrequence);
                crawler.setCreateTime(new Timestamp(new Date().getTime()));
                crawler.setUpdateTime(new Timestamp(new Date().getTime()));
                crawler.setNewPublishDate(new Timestamp(Utils.parse(Utils.getHtmlPublishDate(html)).getTime()));
                crawler.setNewSource(getNewSource(html));

                crawlers.add(crawler);
            }
        }
        return crawlers;
    }



    /**
     * 统计某个网页中某个字段的次数
     * @param text
     * @param keyword
     * @return
     */
    public static Integer getHtmlKeyWordNumber(String text, String keyword){
        Pattern pattern = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        while (matcher.find()){
            count++;
        }
        return count;
    }

    /**
     * 得到新闻的发布日期
     * @param text
     * @return
     */
    public static String getHtmlPublishDate(String text){
        Pattern pattern = Pattern.compile("[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])(\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d)?");
//        Pattern pattern = Pattern.compile("[1-9]\\d{3}\\-(0?[1-9]|1[0-2])\\-(0?[1-9]|[12]\\d|3[01])\\s*(0?[1-9]|1\\d|2[0-3])(\\:(0?[1-9]|[1-5]\\d)){2}");
//        Pattern pattern = Pattern.compile("[1-9]\\d{3}-(0?[1-9]|1[0-2])-(0?[1-9]|[12]\\d|3[01])");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()){
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 得到新闻的来源
     * @param text
     * @return
     */
    public static String getNewSource(String text){
        Pattern pattern = Pattern.compile("来源.+");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()){
            return matcher.group(0).replaceAll("<([^>]*)>", "").replaceAll("\\&[a-zA-Z]{1,10};","").replaceAll("\\|","");
        }
        return "";
    }

    public static String getHtmlBody(String text){
        Document document = Jsoup.parse(text);
        Elements elements = document.getElementsByClass("class\\=\\\".*(main|text|context).*\\\"");
        Pattern pattern = Pattern.compile("class\\=\\\".*(main|text|context).*\\\"");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()){
            return matcher.group(0);
        }
        return "";

    }



    public static Integer getTitleKeywordNumber(String text, String keyword){
        Pattern pattern = Pattern.compile("<title>(.*?)</title>");
        Matcher matcher = pattern.matcher(text);
        String title = "";
        int count = 0;
        while (matcher.find()){
            title = matcher.group(1);
        }
        return getHtmlKeyWordNumber(title, keyword);
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

        HttpURLConnection connection1 = null;
        HttpURLConnection connection = null;

        StringBuffer sb = new StringBuffer();
        BufferedReader reader = null;

       try{
           connection1 = Utils.getHttpURLConnection(url);
           String charset = getCharset(connection1);
           connection = Utils.getHttpURLConnection(url);
           if(connection.getResponseCode() == 200){

               String line;
               reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));

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

    public static String getUUID32(){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        return uuid;
    }

    public static byte[] readInputStream(InputStream inputStream) throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1){
            out.write(buffer, 0, len);
        }

        inputStream.close();
        return out.toByteArray();
    }

    public static Date parse(String str){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(str == null){
                return null;
            }
            return sdf.parse(str);
        }catch (ParseException e){
            throw new RuntimeException("日期解析失败");
        }
    }


    public static void main(String[] args)throws Exception{
        URL url = new URL("http://news.163.com/14/1112/11/AARJJ7QG00014AED.html");

        // choose from a set of useful BoilerpipeExtractors...
//        final BoilerpipeExtractor extractor = CommonExtractors.ARTICLE_EXTRACTOR;
        // final BoilerpipeExtractor extractor = CommonExtractors.DEFAULT_EXTRACTOR;
        // final BoilerpipeExtractor extractor = CommonExtractors.CANOLA_EXTRACTOR;
        // final BoilerpipeExtractor extractor = CommonExtractors.LARGEST_CONTENT_EXTRACTOR;

        // choose the operation mode (i.e., highlighting or extraction)
        final HTMLHighlighter hh = HTMLHighlighter.newHighlightingInstance();
        // final HTMLHighlighter hh = HTMLHighlighter.newExtractingInstance();

//        PrintWriter out = new PrintWriter("E:\\intellij\\crawlerpicture\\src\\main\\webapp\\WEB-INF\\tep\\highlighted.html", "UTF-8");
//        out.println("<base href=\"" + url + "\" >");
//        out.println("<meta http-equiv=\"Content-Type\" content=\"text-html; charset=utf-8\" />");
//        out.println(hh.process(url, extractor));
//        out.close();



        final HTMLDocument htmlDoc = HTMLFetcher.fetch(url);
        final TextDocument doc = new BoilerpipeSAXInput(htmlDoc.toInputSource()).getTextDocument();
//        final TextDocument doc = new BoilerpipeSAXInput(new InputSource(url.openStream(""))).getTextDocument();
        String title = doc.getTitle();

        String content = ArticleExtractor.INSTANCE.getText(doc);

        final BoilerpipeExtractor extractor = CommonExtractors.KEEP_EVERYTHING_EXTRACTOR;
        final ImageExtractor ie = ImageExtractor.INSTANCE;

        List<Image> images = ie.process(url, extractor);

        Collections.sort(images);
        String image = null;
        if (!images.isEmpty()) {
            image = images.get(0).getSrc();
        }

    }


}

package com.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by admin on 2018/5/6.
 */
public class ResponseUtils {
    public static void renderJson(HttpServletResponse response, String jsonStr){
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("utf-8");

        PrintWriter writer = null;

        try{
            writer = response.getWriter();
            writer.append(jsonStr);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            if(writer != null){
                writer.close();
            }
        }
    }

    public static void renderJson(HttpServletResponse response, JSONObject json){
        renderJson(response, json.toJSONString());
    }
}

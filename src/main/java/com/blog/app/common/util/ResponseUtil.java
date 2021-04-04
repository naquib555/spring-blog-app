package com.blog.app.common.util;

import com.google.gson.Gson;
import jdk.nashorn.internal.runtime.JSONFunctions;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;

public class ResponseUtil {

    public static void responseJson(ServletResponse response, Object data) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            out = response.getWriter();
            out.println(new Gson().toJson(data));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}

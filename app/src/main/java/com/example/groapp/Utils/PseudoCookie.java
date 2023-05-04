package com.example.groapp.Utils;

import java.util.HashMap;
import java.util.Map;

public class PseudoCookie {
    private static PseudoCookie pseudoCookie;
    Map<String, String> cookies;

    private PseudoCookie(){
        cookies = new HashMap<>();
    }

    public static synchronized PseudoCookie getPseudoCookie(){
        if(pseudoCookie == null){
            pseudoCookie = new PseudoCookie();
        }
        return pseudoCookie;
    }

    public String getCookieValue(String cookieName){
        if(cookies.containsKey(cookieName))
            return cookies.get(cookieName);
        else
            return "";
    }

    public int setCookieValue(String cookieName, String cookieValue){
        int sizeBefore = cookies.size();
        cookies.put(cookieName, cookieValue);
        int sizeAfter = cookies.size();

        return sizeAfter - sizeBefore;
    }
}
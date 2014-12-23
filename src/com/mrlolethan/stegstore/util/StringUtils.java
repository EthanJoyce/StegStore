package com.mrlolethan.stegstore.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    
    public static String getBetween(String str, String tag) {
        tag = Pattern.quote(tag);
        Pattern p = Pattern.compile(tag + "(.*?)" + tag);
        Matcher m = p.matcher(str);
        if(m.find()) {
            return m.group(1);
        }
        return null;
    }
    
    
}

package com.dionst.service.utils;

public abstract class CommonUtils {

    public static String camelToSnake(String camel)
    {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < camel.length(); i++)
        {
            if ('A' <= camel.charAt(i) && camel.charAt(i) <= 'Z')
            {
                res.append((char) (camel.charAt(i) - ('A' - 'a')));
            }
            else
                res.append(camel.charAt(i));
        }
        return res.toString();
    }
}

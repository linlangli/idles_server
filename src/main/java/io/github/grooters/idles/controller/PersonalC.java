package io.github.grooters.idles.controller;

import io.github.grooters.idles.utils.Printer;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonalC {

    public String getPlace(String url){
        try {
            URL urlObj = new URL(url);
            URLConnection connection = urlObj.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String temp;
            StringBuilder stringBuilder = new StringBuilder();

            while ((temp = reader.readLine()) != null){
                stringBuilder.append(temp);
            }
            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> searchUniversity(String content, String key){

        String reg = "(?<=, \")" + key + "[\\u4e00-\\u9fa5]+[学院大学]";
        Pattern patten = Pattern.compile(reg);
        Matcher matcher = patten.matcher(content);

        List<String> results = new ArrayList<>();

        while (matcher.find()){
            results.add(matcher.group());
        }

        if(results.size() == 0 && Pattern.matches("(?<=, \")" + key, content)){
            results.add(key);
            return results;
        }
        return results;
    }
}

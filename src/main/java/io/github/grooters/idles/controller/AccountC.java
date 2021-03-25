package io.github.grooters.idles.controller;

import io.github.grooters.idles.utils.FileIOer;
import io.github.grooters.idles.utils.Printer;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class AccountC {


    public String createToken(int length){

        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder stringBuilder=new StringBuilder();

        for(int i=0; i< length; i++){
            int number=random.nextInt(str.length());
            stringBuilder.append(str.charAt(number));
        }

        Printer.print("test", "createToken");

        Printer.print("账号信息", "随机token生成：" + stringBuilder.toString());

        return stringBuilder.toString();
    }

    public String createVerification(int length){

        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder stringBuilder=new StringBuilder();

        for(int i=0; i< length; i++){
            int number=random.nextInt(str.length());
            stringBuilder.append(str.charAt(number));
        }

        Printer.print("verification", stringBuilder.toString());

        return stringBuilder.toString();
    }

    public String createRandomNumber(){

        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        String time = String.valueOf(System.currentTimeMillis());
        StringBuilder stringBuilder=new StringBuilder(time);

        Printer.print("账号信息", "随机账号生成：" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    public String createRandomName(){

        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        Random random=new Random();
        StringBuilder stringBuilder=new StringBuilder();

        for(int i=0; i < 5; i++){
            int number=random.nextInt(str.length());
            stringBuilder.append(str.charAt(number));
        }
        Printer.print("账号信息", "随机姓名生成：" + stringBuilder.toString());

        return stringBuilder.toString();
    }

    public long getNetworkTime() {

        try {

            URL url=new URL("http://www.baidu.com");

            URLConnection conn=url.openConnection();

            conn.connect();

            return conn.getDate();

        }catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

}

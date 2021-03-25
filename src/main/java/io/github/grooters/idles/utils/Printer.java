package io.github.grooters.idles.utils;

public class Printer {

    public static void print(String tag, String...content){

        System.out.println("\n-------"+Thread.currentThread().getStackTrace()[2].getLineNumber() +" | "+tag+"------\n");

        for(String str:content){

            System.out.println(str + "\n");

        }

        System.out.println("-------------------------\n");

    }

}

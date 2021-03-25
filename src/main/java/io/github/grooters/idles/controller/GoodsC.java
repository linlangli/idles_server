package io.github.grooters.idles.controller;

import io.github.grooters.idles.bean.User;

public class GoodsC {

    public String[] addNewGoods(User user, String number){

        String[] temp;

        if(user.getMyGoodsPushNumber() != null){

            temp = new String[user.getMyGoodsPushNumber().length + 1];

            temp[user.getMyGoodsPushNumber().length] = number;

        }else{

            temp = new String[]{number};

        }

        return temp;

    }
}

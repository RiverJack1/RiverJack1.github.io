package com.atriver.QQSys.QQClient.QQClientApp;

import com.atriver.QQSys.QQClient.View.QQClientView;
import org.junit.Test;

import java.util.Date;

/**
 * @anthor river
 * @creat 2022-08-02-8:53
 */
public class QQAPP {
    public static void main(String[] args) {
        QQClientView qqClientView=new QQClientView();
        qqClientView.mainView();
    }
    @Test
    public void test(){
        System.out.println(new Date().toString());
    }
}

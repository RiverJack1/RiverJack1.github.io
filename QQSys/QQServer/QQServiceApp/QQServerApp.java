package com.atriver.QQSys.QQServer.QQServiceApp;

import com.atriver.QQSys.QQClient.Service.QQClientPortService;
import com.atriver.QQSys.QQServer.Service.QQServerPortService;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * @anthor river
 * @creat 2022-08-02-10:03
 */
public class QQServerApp {
    public static void main(String[] args) {
        QQServerPortService qqServerPortService=new QQServerPortService();
    }
    @Test
    public void test(){
        Set<String> set=new HashSet<>();
        set.add("小明");
        set.add("小红");
        set.add("小gang");
        set.add("小chui");
        String s1="";
        for (String s : set) {
            s1+="userName: ";
            s1+=s;
            s1+="\n";
        }
        System.out.println(s1);
    }
}

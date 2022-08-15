package com.atriver.QQSys.QQServer.Service;

import com.atriver.QQSys.QQClient.Service.ClientConnectServerThread;
import com.atriver.QQSys.User;

import java.util.HashMap;

/**
 * @anthor river
 * @creat 2022-08-02-9:54
 */
public class ManageServerThread {
    public static HashMap<String, ServerConnectClientThread> serverThreads=new HashMap<>();
    /*
    描述：根据指定的用户名拿到指定的一个线程
    参数：用户名
    返回值：一个客户对应的服务器线程
     */
    public static ServerConnectClientThread getServerConnectClientThreadByUserName(String userName){
        ServerConnectClientThread serverConnectClientThread=null;
        if(serverThreads!=null) {
           serverConnectClientThread=serverThreads.get(userName);
        }
        return serverConnectClientThread;
    }

    /*
    描述：移除指定用户已经无线的线程
    参数： 用户名
    返回值：无返回值
     */
    public static void removeDiedThread(String userName){
        for (ServerConnectClientThread value : serverThreads.values()) {
            if(value.isLoop()==false){
                serverThreads.remove(userName,value);
            }
        }

    }
}

package com.atriver.QQSys.QQClient.Service;

import java.util.HashMap;

/**
 * @anthor river
 * @creat 2022-08-02-9:32
 * @describe 管理客户端的线程
 */
public class ManageClientThread {
    public static HashMap<String,ClientConnectServerThread> clientThreads =new HashMap<>();//存储该用户的线程
    /*
   描述：移除指定用户已经无线的线程
   参数： 用户名
   返回值：无返回值
    */
    public static void removeDiedThread(String userName){
        for ( ClientConnectServerThread value:clientThreads.values()) {
            if(value.isLoop()==false){
                clientThreads.remove(userName,value);
            }
        }
    }
    /*
    描述：根据用户名拿到它持有的一个线程
    参数：用户名
    返回值：该用户的指定一个线程
     */
    public static ClientConnectServerThread getClientConnectServerThreadByUseName(String userName){
        ClientConnectServerThread clientConnectServerThread=null;
        if(clientThreads!=null) {
             clientConnectServerThread = clientThreads.get(userName);
        }
        return clientConnectServerThread;
    }

}

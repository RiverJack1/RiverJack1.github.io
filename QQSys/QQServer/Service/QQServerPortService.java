package com.atriver.QQSys.QQServer.Service;

import com.atriver.QQSys.Message;
import com.atriver.QQSys.MessageStatus;
import com.atriver.QQSys.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @anthor river
 * @creat 2022-08-02-8:58
 * @describe 该类实现qq服务端的相关功能，如验证登录，实现通信等
 */
public class QQServerPortService {
    private ServerSocket sSocket;

    {
        try {
            System.out.println("服务器正在8848端口监听*****");
            sSocket = new ServerSocket(8848);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CopyOnWriteArrayList<User>registerUsers=
        new CopyOnWriteArrayList<>();//用于存放已经注册的用户信息，实现伪数据库的功能

    //一个构造器，初始化注册用户信息
     public QQServerPortService(){
         registerUsers.add(new User("至尊宝","123"));
         registerUsers.add(new User("猪八戒","123"));
         registerUsers.add(new User("周杰伦","123"));
         registerUsers.add(new User("王菲","123"));
         checkUser();
     }
   //相应的get/set方法
    public CopyOnWriteArrayList<User> getRegisterUsers() {
        return registerUsers;
    }

    public void setRegisterUsers(CopyOnWriteArrayList<User> registerUsers) {
        this.registerUsers = registerUsers;
    }
    //验证客户端的信息是否是已经注册的用户
    public void checkUser(){
         while (true) {
             try {
                 Socket socket = sSocket.accept();
                 //读取来自客户端的user对象
                 InputStream is = socket.getInputStream();
                 ObjectInputStream ois = new ObjectInputStream(is);
                 User user = (User) ois.readObject();
                 //如果是已经注册的用户，则返沪给客户端登录成功的消息，并且开启线程,并把它放入线程集合中
                 OutputStream os = socket.getOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(os);
                 Message message = new Message();
                 if (isRegister(user)) {
                     System.out.println(user.getUserName()+"已经与服务器建立连接");
                     message.setMessageType(MessageStatus.登录成功);
                     oos.writeObject(message);
                     ServerConnectClientThread ssct = new ServerConnectClientThread();
                     ssct.setSocket(socket);
                     ssct.start();
                     ManageServerThread.serverThreads.put(user.getUserName(), ssct);
                 } else {
                     message.setMessageType(MessageStatus.登录失败);
                     oos.writeObject(message);
                    socket.close();
                 }
             } catch (IOException e) {
                 e.printStackTrace();
             } catch (ClassNotFoundException e) {
                 e.printStackTrace();
             }
         }


    }
    /*
    描述：验证一个user是否是已经注册的用户
    参数：一个user对象
    返回值：若是，返回true，否则返回false
     */
    public boolean isRegister(User user){
         boolean isRegister=false;
         if(registerUsers.contains(user)){
             isRegister=true;
         }
         return isRegister;
    }
}

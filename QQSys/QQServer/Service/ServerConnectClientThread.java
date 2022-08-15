package com.atriver.QQSys.QQServer.Service;

import com.atriver.QQSys.Message;
import com.atriver.QQSys.MessageStatus;


import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * @anthor river
 * @creat 2022-08-02-9:49
 */
public class ServerConnectClientThread extends Thread{
    private boolean loop=true;//用于控制线程的开始与结束
    private Socket socket;//该线程持有的socket对象
    public ServerConnectClientThread(Socket socket){
        this.socket=socket;
    }
    public ServerConnectClientThread(){};

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    /*
    描述：用于格式化在线用户的信息，格式为username：name
    参数：一个存储了在线用户名的set集合
    返回值：一个经过用户名格式化后的string
     */
    public String onLineUserNames(Set<String> set){
        String s1="";
        for (String s : set) {
            s1+="userName: ";
            s1+=s;
            s1+="\n";
        }
     return s1;
    }

    @Override
    //服务端的线程，用于一直读取来自客户端的消息
    public void run() {
        while (loop){
            try {
                InputStream is = socket.getInputStream();
                ObjectInputStream ois=new ObjectInputStream(is);
                Message message =(Message) ois.readObject();
                if(message.getMessageType()== MessageStatus.客户端退出){
                    Message exitMessage=new Message();
                    message.setReceiver(message.getSender());
                    exitMessage.setMessageType(MessageStatus.客户端退出);
                    OutputStream outputStream = socket.getOutputStream();
                    ObjectOutputStream oos=new ObjectOutputStream(outputStream);
                    oos.writeObject(exitMessage);
                    loop=false;
                    System.out.println(message.getSender()+"断开了连接");
                    socket.close();
                    ManageServerThread.removeDiedThread(message.getSender());
                }
                else if(message.getMessageType()==MessageStatus.请求用户列表){
                    System.out.println(message.getSender()+"正在请求用户列表");
                    String s = onLineUserNames(ManageServerThread.serverThreads.keySet());
                    System.out.println(s);
                    Message nameOfMessage=new Message();
                    nameOfMessage.setMessageContent(s);
                    nameOfMessage.setMessageType(MessageStatus.返回用户列表);
                    nameOfMessage.setReceiver(message.getSender());
                    OutputStream os = socket.getOutputStream();
                    ObjectOutputStream oos=new ObjectOutputStream(os);
                    oos.writeObject(nameOfMessage);
                    System.out.println("返回信息已经发送");
                }
                else if(message.getMessageType()==MessageStatus.群发信息){
                    System.out.println(message.getSender()+"正在群发信息");
                    //遍历所有在线用户的线程集合，拿到对应的socket，发送信息
                    Collection<ServerConnectClientThread> values =
                            ManageServerThread.serverThreads.values();
                    Iterator<ServerConnectClientThread> iterator = values.iterator();
                    while (iterator.hasNext()) {
                        ServerConnectClientThread next = iterator.next();
                        Socket socket = next.getSocket();
                        //排除自己与自己通信
                        if (!socket.equals(ManageServerThread.
                                getServerConnectClientThreadByUserName(message.getSender()).getSocket())) {
                            OutputStream os = socket.getOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(os);
                            oos.writeObject(message);
                        }
                    }
                }
                else if(message.getMessageType()==MessageStatus.私聊消息){
                    System.out.println(message.getSendTime()+" "
                            +message.getSender()+"向"+message.getReceiver()+"发起了私聊");
                    String receiver = message.getReceiver();
                    ServerConnectClientThread scctb = ManageServerThread.
                            getServerConnectClientThreadByUserName(receiver);
                    Socket socket = scctb.getSocket();
                    OutputStream os = socket.getOutputStream();
                    ObjectOutputStream oos=new ObjectOutputStream(os);
                    oos.writeObject(message);
                }
                else if(message.getMessageType()==MessageStatus.发送文件){
                    System.out.println(message.getSender()+"向"+message.getReceiver()+"开始发送文件");
                    Socket socket = ManageServerThread.getServerConnectClientThreadByUserName
                                    (message.getReceiver()).getSocket();
                    OutputStream os = socket.getOutputStream();
                    ObjectOutputStream oos=new ObjectOutputStream(os);
                    oos.writeObject(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

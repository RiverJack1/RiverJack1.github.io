package com.atriver.QQSys.QQClient.Service;

import com.atriver.QQSys.Message;
import com.atriver.QQSys.MessageStatus;

import java.io.*;
import java.net.Socket;

/**
 * @anthor river
 * @creat 2022-08-02-9:23
 */
public class ClientConnectServerThread extends Thread{
    private Socket socket;//该线程持有的socket对象
    private boolean loop=true;//用于控制线程的开始与结束
    private static String onLineUserNames;
    //一个构造器用于初始化socket对象
    public ClientConnectServerThread(Socket socket){
        this.socket=socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public static String getOnLineUserNames() {
        return onLineUserNames;
    }

    public static void setOnLineUserNames(String onLineUserNames) {
        onLineUserNames = onLineUserNames;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    @Override
    //一个线程，用于客户读取来自服务器的消息
    public void run() {
        while (loop) {
            try {
                InputStream is = socket.getInputStream();
                ObjectInputStream ois=new ObjectInputStream(is);
                Message message =(Message) ois.readObject();
                if(message.getMessageType()== MessageStatus.返回用户列表){
                   onLineUserNames = message.getMessageContent();
                }
                else if(message.getMessageType()==MessageStatus.客户端退出){
                    loop=false;
                    socket.close();
                    ManageClientThread.removeDiedThread(message.getReceiver());
                }
                else if(message.getMessageType()==MessageStatus.群发信息){
                    System.out.println("时间："+message.getSendTime());
                    System.out.println(message.getSender()+"对大家说："+message.getMessageContent());
                }
                else if(message.getMessageType()==MessageStatus.私聊消息){
                    System.out.println("时间："+message.getSendTime());
                    System.out.println(message.getSender()+"对你说："+message.getMessageContent());
                }
                else if(message.getMessageType()==MessageStatus.发送文件){
                    String fileContent=message.getMessageContent();
                    byte[] bytes = fileContent.getBytes();
                    File file=new File("d:\\12.png");
                    BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
                    bos.write(bytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

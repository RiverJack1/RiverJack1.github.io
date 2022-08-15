package com.atriver.QQSys.QQClient.Service;

import com.atriver.QQSys.Message;
import com.atriver.QQSys.MessageStatus;
import com.atriver.QQSys.QQClient.Utilities.CMUtility;
import com.atriver.QQSys.User;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;


/**
 * @anthor river
 * @creat 2022-08-02-9:07
 * @describe 实现了qq客户端服务的相关功能，如验证登录，私聊群发等。
 */
public class QQClientPortService {
    private Socket socket;
    private  String onLineUserNames;
    /*
    描述：实现某个客户对某个客户的文件发送
    参数：filePath->文件路径；sender->发生着；receiver->接收者
    返回值：无
     */
    public void toOneFile(String filePass,String sender,String rcveiver){
       Message message=new Message();
       message.setMessageType(MessageStatus.发送文件);
        byte cbuf[]=new byte[1024];
        int len;
        message.setSender(sender);
        message.setReceiver(rcveiver);
        String fileMessage="";
        try {
            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(new File(filePass)));
            OutputStream os = socket.getOutputStream();
            while ((len=bis.read(cbuf))!=-1){
                fileMessage+=new String(cbuf,0,len);
            }
            message.setMessageContent(fileMessage);
            ObjectOutputStream oos=new ObjectOutputStream(os);
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    /*
    说明：某个客户向服务器请求在线用户列表
    参数：用户名
    返回值：无
     */
    public void showOnlineUsers(String userName){
        Message message=new Message();
        message.setMessageType(MessageStatus.请求用户列表);
        message.setSender(userName);
        try {
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(os);
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    描述：实现某个客户与某个客户的私聊
    参数：发送者名，接收者名
    返回值：无
     */
    public void toOneMessage(String sender,String receiver){
        Message message=new Message();
        message.setMessageType(MessageStatus.私聊消息);
        String privateMessage=CMUtility.readString(100);
        message.setMessageContent(privateMessage);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSendTime(new Date().toString());
        try {
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(os);
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    描述：实现某个用户向在线用户群发信息
    参数：该用户名
    返回值：无
     */
    public void toAnyOneMessage(String userName){
        Message message=new Message();
        message.setMessageType(MessageStatus.群发信息);
        message.setSender(userName);
        System.out.println("输入你要群发的信息：");
        String commonMessage= CMUtility.readString(100);
        message.setMessageContent(commonMessage);
        message.setSendTime(new Date().toString());
        try {
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(os);
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    描述：用于某个客户端的退出，向服务器发出一个退出指令，让服务器的线程集合移除该线程
    参数：退出系统的用户名
    返回值：无返回值
     */
    public void exitSystem(String userName){
        Message message=new Message();
        message.setMessageType(MessageStatus.客户端退出);
        message.setSender(userName);
        try {
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(os);
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    说明：检查该用户是否在服务器端中注册用户中，如果是返回true，不是返回false，用于验证登录
    参数：用户名，密码
    返回值：true/false
     */
    public boolean checkUser(String userName,String passWord){
        boolean isLogin=false;//记录返回值
        //封装为user对象，以序列化方式发送
        User user=new User(userName,passWord);
        Message message = null;
        try {
            socket = new Socket(InetAddress.getByName("192.168.1.6"),8848);
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(os);
            oos.writeObject(user);
            //接收服务器回发的消息
            InputStream is = socket.getInputStream();
            ObjectInputStream ois=new ObjectInputStream(is);
            message = (Message)ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //该用户在服务器存在，则登陆成功，则开启一个线程，用于一直读取来自服务器端的信息,并把这个线程加入到集合中
        //防止登录失败时，抛出空指针异常
            if (message.getMessageType() == MessageStatus.登录成功) {
                ClientConnectServerThread ccst = new ClientConnectServerThread(socket);
                ccst.start();
                ManageClientThread.clientThreads.put(userName, ccst);
                isLogin = true;
            } else if(message.getMessageType()==MessageStatus.登录失败) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return isLogin;
    }
}

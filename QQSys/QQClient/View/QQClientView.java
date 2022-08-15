package com.atriver.QQSys.QQClient.View;

import com.atriver.QQSys.QQClient.Service.ClientConnectServerThread;
import com.atriver.QQSys.QQClient.Service.ManageClientThread;
import com.atriver.QQSys.QQClient.Service.QQClientPortService;
import com.atriver.QQSys.QQClient.Utilities.CMUtility;

import java.util.ArrayList;

/**
 * @anthor river
 * @creat 2022-07-31-14:44
 */
public class QQClientView {
    private boolean loop=true;
    String userName;
    String passWord;
    QQClientPortService qqcps=new QQClientPortService();
    //界面主菜单
    public void mainView(){
        while (loop) {
            System.out.println("============欢迎登录网络通信系统===========");
            System.out.println("\t\t\t1 登录系统 ");
            System.out.println("\t\t\t9 退出系统 ");
            System.out.print("请输入你的选择： ");
            int menuSelet = CMUtility.readInt();
            switch (menuSelet) {
                case 1:
                    System.out.print("请输入用户号：");
                     userName = CMUtility.readString(20);
                    System.out.print("请输入密码：");
                     passWord = CMUtility.readString(20);
                    if (qqcps.checkUser(userName,passWord)) {
                        System.out.println("===========欢迎"+userName+"用户===========");
                        seconedView();
                    } else {
                        //登录失败重新输入
                    }
                    break;
                case 9:
                    loop=false;

                    break;
            }
        }
    }
    //界面二级菜单
    public void seconedView(){
        boolean loop=true;
        while (loop) {
            System.out.println("=========网络通信二级菜单(用户 " + userName + ")============");
            System.out.println("1:显示在线用户列表");
            System.out.println("2:群发消息");
            System.out.println("3:私聊消息");
            System.out.println("4:发送文件");
            System.out.println("9:退出系统");
            System.out.println("请输入你的选择: ");
            int viewSelect = CMUtility.readInt();
            switch (viewSelect) {
                case 1:
                    System.out.println("====当前用户在线列表====");
                    qqcps.showOnlineUsers(userName);
                    if(ClientConnectServerThread.getOnLineUserNames()!=null) {
                        System.out.println(ClientConnectServerThread.getOnLineUserNames());
                    }
                    else {
                        System.out.println("请再次刷新");
                    }
                    break;
                case 2:
                    System.out.println("=====群发消息======");
                    qqcps.toAnyOneMessage(userName);
                    break;
                case 3:
                    System.out.println("====私聊消息====");
                    System.out.print("你要私聊的用户名（在线）: ");
                    String reveiver=CMUtility.readString(20);
                    qqcps.toOneMessage(userName,reveiver);
                    break;
                case 4:
                    System.out.println("=====发送文件======");
                    System.out.print("你要发送的用户:");
                    String receiver=CMUtility.readString(100);
                    System.out.print("你要发送的文件路径: ");
                    String filePass=CMUtility.readString(100);
                    qqcps.toOneFile(filePass,userName,receiver);
                    break;
                case 9:
                    qqcps.exitSystem(userName);
                    loop = false;
                    break;
            }
        }
    }


}

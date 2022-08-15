package com.atriver.QQSys;

import java.io.Serializable;
import java.util.Objects;

/**
 * @anthor river
 * @creat 2022-07-31-15:37
 */
public class User implements Serializable {
    private static final long seriaVersionUid=18l;
    private String userName;//用户姓名
    private String passWord;//用户密码

    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName) && Objects.equals(passWord, user.passWord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, passWord);
    }
}

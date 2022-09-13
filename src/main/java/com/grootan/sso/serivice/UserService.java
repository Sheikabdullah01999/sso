package com.grootan.sso.serivice;

import com.grootan.sso.model.UserInfo;

public interface UserService {
    public StringBuffer userInfo(String idToken);
    public String addUser(String email,String userName);
    public void update(String oldEmail,String newEmail);
}

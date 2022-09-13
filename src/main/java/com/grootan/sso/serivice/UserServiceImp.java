package com.grootan.sso.serivice;

import antlr.StringUtils;
import com.grootan.sso.model.UserInfo;
import com.grootan.sso.repository.UserRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public StringBuffer userInfo(String idToken) {

        StringBuffer userInfo = new StringBuffer();
        String[] parts = idToken.split("\\.");

        JSONObject payload = new JSONObject(decode(parts[1]));
        String userName = payload.getString("name");
        String email = payload.getString("email");
        userInfo.append("Welcome, " + userName + "<br><br>");
        userInfo.append("e-mail: " + email + "<br><br>");
        UserInfo userInformation = new UserInfo(userName, email);
//        String existsUserName = userRepo.findByName(userName);


//            if (existsUserName == null && !userName.equals(existsUserName) ) {
//                userRepo.save(userInformation);
//            }


        return userInfo;
    }

    @Override
    public String addUser(String email,String userName) {
        UserInfo userInformation = new UserInfo(userName, email,System.currentTimeMillis());

        String existsUserName = userRepo.findByName(userName);

//            if (existsUserName == null && !userName.equals(existsUserName) ) {
                userRepo.save(userInformation);
//                return "success";
//            }
            return "save failed";
    }

    @Override
    public void update(String oldEmail, String newEmail) {

    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }
}

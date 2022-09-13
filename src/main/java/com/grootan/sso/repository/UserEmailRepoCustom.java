package com.grootan.sso.repository;

import com.grootan.sso.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserEmailRepoCustom
{
    public List<UserInfo> getUserEmail(String email);
    void updateEmail(String email,String newEmail);
    List<UserInfo> selectMyClassByDate(Date date);

}


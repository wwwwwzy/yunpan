package com.javatest.yunpan.service;

import com.github.pagehelper.PageInfo;
import com.javatest.yunpan.dto.UserInfo;
import com.javatest.yunpan.entity.BasicUserInfo;
import com.javatest.yunpan.entity.MoreUserInfo;

import java.util.Optional;

public interface UserService {

    Optional<Integer> login(BasicUserInfo basicUserInfo);

    int sign(BasicUserInfo basicUserInfo);

    Optional<String> emailCheck(String email);

    PageInfo<BasicUserInfo> findUsers(int pageNum);

    UserInfo findUserInfo(int id);

    int finishUserInfo(MoreUserInfo moreUserInfo);

    int updateUserInfo(MoreUserInfo moreUserInfo);

    int accountVertify(String email);
}

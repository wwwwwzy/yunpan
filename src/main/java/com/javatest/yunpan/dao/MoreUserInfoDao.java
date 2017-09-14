package com.javatest.yunpan.dao;

import com.javatest.yunpan.dto.UserInfo;
import com.javatest.yunpan.entity.MoreUserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MoreUserInfoDao {

    UserInfo findUserInfo(int id);

    int finishUserInfo(MoreUserInfo moreUserInfo);

    int updateUserInfo(MoreUserInfo moreUserInfo);

    int accoutnVertify(String email);
}

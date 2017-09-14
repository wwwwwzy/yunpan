package com.javatest.yunpan.dao;

import com.javatest.yunpan.entity.BasicUserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BasicUserInfoDao {

    Integer findOne(BasicUserInfo basicUserInfo);

    int insertOne(BasicUserInfo basicUserInfo);

    String selectEmailByEmail(String email);

    List<BasicUserInfo> findMany();
}

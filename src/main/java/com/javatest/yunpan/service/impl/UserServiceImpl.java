package com.javatest.yunpan.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javatest.yunpan.dao.BasicUserInfoDao;
import com.javatest.yunpan.dao.MoreUserInfoDao;
import com.javatest.yunpan.dto.UserInfo;
import com.javatest.yunpan.entity.BasicUserInfo;
import com.javatest.yunpan.entity.MoreUserInfo;
import com.javatest.yunpan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BasicUserInfoDao basicUserInfoDao;
    @Autowired
    private MoreUserInfoDao moreUserInfoDao;

    @Override
    public Optional<Integer> login(BasicUserInfo basicUserInfo) {
        return Optional.ofNullable(basicUserInfoDao.findOne(basicUserInfo));
    }

    @Override
    public int sign(BasicUserInfo basicUserInfo) {
        try {
            basicUserInfoDao.insertOne(basicUserInfo);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Optional<String> emailCheck(String email) {
        return Optional.ofNullable(basicUserInfoDao.selectEmailByEmail(email));
    }

    @Override
    public PageInfo<BasicUserInfo> findUsers(int pageNum) {
        return PageHelper.startPage(pageNum, 15).doSelectPageInfo(() -> basicUserInfoDao.findMany());
    }

    @Override
    public UserInfo findUserInfo(int id) {
        return moreUserInfoDao.findUserInfo(id);
    }

    @Override
    public int finishUserInfo(MoreUserInfo moreUserInfo) {
        return moreUserInfoDao.finishUserInfo(moreUserInfo);
    }

    @Override
    public int updateUserInfo(MoreUserInfo moreUserInfo) {
        return moreUserInfoDao.updateUserInfo(moreUserInfo);
    }

    @Override
    public int accountVertify(String email) {
        return moreUserInfoDao.accoutnVertify(email);
    }
}

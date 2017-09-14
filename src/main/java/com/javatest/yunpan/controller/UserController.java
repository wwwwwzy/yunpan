package com.javatest.yunpan.controller;

import com.github.pagehelper.PageInfo;
import com.javatest.yunpan.dto.ResponseResult;
import com.javatest.yunpan.dto.ResponseResultFactory;
import com.javatest.yunpan.dto.UserInfo;
import com.javatest.yunpan.entity.BasicUserInfo;
import com.javatest.yunpan.entity.MoreUserInfo;
import com.javatest.yunpan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 用户登录
     * @param basicUserInfo
     * @return
     */
    @RequestMapping(value = "/session", method = RequestMethod.POST)
    public ResponseResult login(@RequestBody BasicUserInfo basicUserInfo) {
        Optional<Integer> id = userService.login(basicUserInfo);
        if (id.isPresent()) {
            httpServletRequest.getSession().setAttribute("userId", id.get());
            return ResponseResultFactory.getResponseResult(true, "登录成功");
        }
        return ResponseResultFactory.getResponseResult(false, "用户名或密码错误");
    }

    /**
     * 用户退出
     * @param id
     * @return
     */
    @RequestMapping(value = "/session/{id}", method = RequestMethod.DELETE)
    public ResponseResult loginOut(@PathVariable("id") int id) {
        if (httpServletRequest.getSession().getAttribute("userId") != null
                && (int)httpServletRequest.getSession().getAttribute("userId") == id) {
            httpServletRequest.getSession().setAttribute("userId", "");
            return ResponseResultFactory.getResponseResult(true, "退出成功");
        }
        return ResponseResultFactory.getResponseResult(false, "用户未登录");
    }

    /**
     * 验证邮箱是否注册
     * @param email
     * @return
     */
    @RequestMapping(value = "/email/{email}/", method = RequestMethod.GET)
    public ResponseResult emailCheck(@PathVariable String email) {
        if (userService.emailCheck(email).isPresent()) {
            return ResponseResultFactory.getResponseResult(false, "邮箱已注册");
        }
        return ResponseResultFactory.getResponseResult(true, "邮箱未注册");
    }

    /**
     * 控制台分页获取用户列表
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public PageInfo<BasicUserInfo> getUserList(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum) {
        return userService.findUsers(pageNum);
    }

    /**
     * 用户注册，验证由前端完成，接收验证后的信息
     * @param basicUserInfo
     * @return
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseResult sign(@RequestBody BasicUserInfo basicUserInfo) {
        if (userService.sign(basicUserInfo) > 0) {
            return ResponseResultFactory.getResponseResult(true, "注册成功");
        };
        return ResponseResultFactory.getResponseResult(false, "注册失败");
    }

    /**
     * 获取用户详细信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseResult<UserInfo> getUserInfo(@PathVariable("id") int id) {
        UserInfo userInfo = userService.findUserInfo(id);
        if (userInfo != null) {
            return ResponseResultFactory.getResponserResult(true, userInfo);
        }
        return ResponseResultFactory.getResponserResult(false, "用户信息未完善");
    }

    /**
     * 注册用户完善信息
     * @param moreUserInfo
     * @return
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.POST)
    public ResponseResult finishUserInfo(@RequestBody MoreUserInfo moreUserInfo) {
        if (userService.finishUserInfo(moreUserInfo) > 0) {
            return ResponseResultFactory.getResponserResult(true, "信息填写完成");
        }
        return ResponseResultFactory.getResponserResult(false, "信息填写失败");
    }

    /**
     * 更新用户信息
     * @param moreUserInfo
     * @return
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public ResponseResult updateUserInfo(@RequestBody MoreUserInfo moreUserInfo) {
        if (userService.updateUserInfo(moreUserInfo) > 0) {
            return ResponseResultFactory.getResponserResult(true, "更新信息成功");
        }
        return ResponseResultFactory.getResponserResult(false, "更新信息失败");
    }

    /**
     * 邮箱激活
     * @param email
     * @return
     */
    @RequestMapping(value = "/user/{email}/", method = RequestMethod.PATCH)
    public ResponseResult accountVertify(@PathVariable String email) {
        if (userService.accountVertify(email) > 0) {
            return ResponseResultFactory.getResponseResult(true, "邮箱激活成功");
        }
        return ResponseResultFactory.getResponseResult(false, "邮箱激活失败");
    }
}

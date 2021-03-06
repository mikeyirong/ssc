package com.project.controller.wechat;

import com.project.common.exception.BusinessException;
import com.project.common.exception.ExceptionEnum;
import com.project.common.result.Result;
import com.project.common.result.ResultBuilder;
import com.project.common.util.LogUtil;
import com.project.model.sql.User;
import com.project.model.sql.UserRelation;
import com.project.model.vo.Page;
import com.project.service.weixin.user.WechatUserReleationService;
import com.project.service.weixin.user.WechatUserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by goforit on 2017/11/27.
 */
@Controller
@RequestMapping("/wechat/user")
public class WechatUserController {

    @Autowired
    private WechatUserService wechatUserService;
    @Autowired
    private WechatUserReleationService wechatUserReleationService;

    Logger logger = LogUtil.getLogger(getClass());

    @RequestMapping("/list")
    public String list(Page page, Model model) {
        try {
            //设置Url
            page.setUrl("/wechat/user/list");
            List<User> list = wechatUserService.list(page);
            model.addAttribute("page", page);
            model.addAttribute("list", list);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return "userlist";
    }

    @RequestMapping("/releation")
    public String releation(Page page, Model model) {
        try {
            //设置Url
            page.setUrl("/wechat/user/releation");
            List<UserRelation> list = wechatUserReleationService.list(page);
            model.addAttribute("page", page);
            model.addAttribute("list", list);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return "releationlist";
    }

    @RequestMapping(value = "/listjson")
    @ResponseBody
    public Result listjson(Page page) {
        try {
            //设置Url
            page.setUrl("/wechat/user/listjson");
            List<User> list = wechatUserService.list(page);
            return ResultBuilder.build(ExceptionEnum.WECHAT_USER_LIST,list);
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            return e.getResult();
        }

    }

    @RequestMapping(value = "/releationjson")
    @ResponseBody
    public Result releationjson(Page page) {
        try {
            //设置Url
            page.setUrl("/wechat/user/releationjson");
            List<UserRelation> list = wechatUserReleationService.list(page);
            return ResultBuilder.build(ExceptionEnum.WECHAT_USERRELEATION_LIST,list);
        } catch (BusinessException e) {
            logger.error(e.getMessage(),e);
            return e.getResult();
        }

    }


//    @RequestMapping("/log")
//    public String releation(Page page, Model model) {
//        try {
//            //设置Url
//            page.setUrl("/wechat/user/releation");
//            List<UserRelation> list = wechatUserReleationService.list(page);
//            model.addAttribute("page", page);
//            model.addAttribute("list", list);
//        } catch (BusinessException e) {
//            e.printStackTrace();
//        }
//        return "loglist";
//    }

}

package com.example.euniversity.utils

enum class ResultEnum(val code:Int,val msg:String) {
    REGISTER_SUCCESS(200,"注册成功"),
    FIND_SUCCESS(201,"查询成功"),
    LOGIN_SUCCESS(202,"登陆成功"),
    RETRIEVE_PASSWORD_SUCCESS(203,"找回密码成功"),
    CHANGE_PASSWORD_SUCCESS(203,"修改密码成功"),
    UPDATE_PROFILE_INFORMATION_SUCCESS(204,"更新个人信息成功"),
    ASK_PROBLEM_SUCCESS(205,"提交问题成功"),
    USER_NOT_EXIST(400,"用户不存在"),
    USER_IS_EXIST(401,"用户已存在"),
    REGISTER_FAILD(402,"注册失败"),
    INPUT_IS_NULL(403,"提交数据为空"),
    PASSWORD_IS_WRONG(404,"密码错误"),
    RETRIEVE_PASSWORD_FAILD(405,"修改密码失败"),
    UPDATE_PROFILE_INFORMATION_FAILD(406,"更新个人信息失败"),
    NO_PROBLEMS_IN_DATABASE(407,"数据库中没有问题数据"),
    ASK_PROBLEM_FAILD(408,"提交问题失败");
}
package azone.dao;

import azone.pojo.User;

public interface UserDAO {
    User getUser(String uname, String pwd);// 通过 这两个 参数 去数据库获取

}

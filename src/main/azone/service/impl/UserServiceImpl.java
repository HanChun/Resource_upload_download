package azone.service.impl;

import azone.dao.UserDAO;
import azone.pojo.User;
import azone.service.UserService;

public class UserServiceImpl implements UserService {
    private UserDAO userDAO ;
    @Override
    public User login(String uname, String pwd) {
        return userDAO.getUser(uname,pwd);
    }
}

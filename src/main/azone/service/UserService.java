package azone.service;

import azone.pojo.User;

public interface UserService {
    User login(String uname, String pwd);
}

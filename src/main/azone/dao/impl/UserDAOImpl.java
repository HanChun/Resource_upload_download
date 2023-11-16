package azone.dao.impl;

import myssm.basedao.BaseDAO;
import azone.dao.UserDAO;
import azone.pojo.User;
public class UserDAOImpl extends BaseDAO<User> implements UserDAO {
    @Override
    public User getUser(String uname, String pwd) {
        return load("select * from t_user where uname like ? and pwd like ?",uname,pwd);
    }
}

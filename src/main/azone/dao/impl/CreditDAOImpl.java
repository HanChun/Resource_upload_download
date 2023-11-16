package azone.dao.impl;

import azone.dao.CreditDAO;
import azone.pojo.User;
import myssm.basedao.BaseDAO;

public class CreditDAOImpl extends BaseDAO<CreditDAO> implements CreditDAO {
    @Override
    public void addOneCredit(User user) {
        executeUpdate("update t_user set credit = credit + 6 where user_id = ?",  user.getUser_id());
    }
    @Override// 当 其它用户 下载了 当前用户上传的 资源，当前用户的账户的 credit+9
    public void addDownloadCredit(String fileName){
        executeUpdate("update t_user set credit = credit + 9 where uname = " +
                "(select distinct \"publishAuthor\" from t_downloadresource where \"resourceName\"=?)", fileName);
    }
    @Override
    public void decreaseOneCredit(User user) {
        executeUpdate("update t_user set credit = credit - 3 where user_id = ?",user.getUser_id());
    }
    @Override
    public void updateCredit(User user,int credit){
        executeUpdate("update t_user set credit = ? where user_id = ?", credit, user.getUser_id());
    }
    @Override
    public int getUserCredit(User user) {
        int result =  ((Integer)super.executeComplexQuery("select credit from t_user where user_id=?",user.getUser_id())[0]).intValue();
        System.out.println(" result : " + result);
        return result;
    }
}













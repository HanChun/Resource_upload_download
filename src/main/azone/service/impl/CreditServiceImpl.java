package azone.service.impl;

import azone.dao.CreditDAO;
import azone.pojo.User;
import azone.service.CreditService;

public class CreditServiceImpl implements CreditService {
    private CreditDAO creditDAO;
    @Override
    public void addOneCredit(User user) {
        creditDAO.addOneCredit(user);
    }

    @Override
    public String decreaseCredit(User user) {
        int credit = creditDAO.getUserCredit(user);
        if( credit > 0 ){
            creditDAO.decreaseOneCredit(user);
            return "succeed";
        } else {
            return "false";
        }
    }

    @Override
    public Integer getUserCredit(User user) {
        int credit = creditDAO.getUserCredit(user);
        return credit;
    }

    @Override
    public void updateCredit(User user, int credit) {
        creditDAO.updateCredit(user,credit);
    }

    @Override
    public void addDownloadCredit(String fileName){ creditDAO.addDownloadCredit(fileName); };


}









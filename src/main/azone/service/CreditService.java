package azone.service;

import azone.pojo.User;

public interface CreditService {
    public void addOneCredit(User user);
    public String decreaseCredit(User user);
    public Integer getUserCredit(User user);
    public void updateCredit(User user,int credit) ;

    public void addDownloadCredit(String fileName);
}

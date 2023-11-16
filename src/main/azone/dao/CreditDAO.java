package azone.dao;

import azone.pojo.User;

public interface CreditDAO {
    void addOneCredit(User user);
    void decreaseOneCredit(User user);
    int getUserCredit(User user);
    void updateCredit(User user, int credit);
    void addDownloadCredit(String fileName);
}

package Service;

import DAO.SocialMediaDao;
import Model.Account;

public class SocialMediaServices{
    private SocialMediaDao dao;

    public void setDao (SocialMediaDao dao){
        this.dao = dao;
    }
    
    public Account serviceRegister (Account newAccount){
        if (newAccount.username == null || newAccount.username.strip().length() == 0 || newAccount.password.length() < 4){
            //throw InvalidEntryException
            return null;
        }
        return dao.daoRegister(newAccount);
    }

    public Account serviceLogin (Account existingAccount) {
        return dao.daoLogin(existingAccount);
    }
}
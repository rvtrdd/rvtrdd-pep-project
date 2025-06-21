package Service;

import DAO.SocialMediaDao;
import Model.*;

public class SocialMediaServices{
    private SocialMediaDao dao;

    public void setDao (SocialMediaDao dao){
        this.dao = dao;
    }
    
    public Account serviceRegister (Account newAccount){
        if (newAccount.username.isBlank() || newAccount.password.length() < 4){
            //throw InvalidEntryException
            return null;
        }
        return dao.daoRegister(newAccount);
    }

    public Account serviceLogin (Account existingAccount) {
        return dao.daoLogin(existingAccount);
    }

    public Message serviceCreateMessage(Message message){
        if (message.message_text.isBlank() || message.message_text.length() >= 255){
            return null;
        }
        return dao.daoCreateMessage(message);
    }


}
package Service;

import java.util.List;

import DAO.SocialMediaDao;
import Model.*;

public class SocialMediaServices{
    private SocialMediaDao dao;

    public void setDao (SocialMediaDao dao){
        this.dao = dao;
    }
    
    public Account registerUser (Account newAccount){
        if (newAccount.username.isBlank() || newAccount.password.length() < 4){
            //throw InvalidEntryException
            return null;
        }
        return dao.daoRegister(newAccount);
    }

    public Account loginUser (Account existingAccount) {
        return dao.daoLogin(existingAccount);
    }

    public Message createMessage(Message message){
        if (message.message_text.isBlank() || message.message_text.length() >= 255){
            return null;
        }
        return dao.daoCreateMessage(message);
    }

    public Message updateMessage(Message message) {
        if (message.message_text.isBlank() || message.message_text.length() >= 255){
            return null;
        }
        return dao.updateMessage(message);
    }

    public Message deleteMessage(Message message) {
        return dao.deleteMessage(message);
    }

    public Message getMessage(Message message) {
        return dao.getMessage(message);
    }

    public List<Message> getAllMessages() {
        return dao.getAllMessages();
    }

    public List<Message> getAllMessagesFromUser(Message message){
        return dao.getAllMessagesFromUser(message);
    }



}
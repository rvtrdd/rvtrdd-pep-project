package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class SocialMediaDao {


    public Account daoRegister (Account account){
        Account result = null;
        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "INSERT INTO account (username, password) VALUES(?, ?); ";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.username);
            ps.setString(2, account.password);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()){
                int generatedKey = generatedKeys.getInt(1);
                result = new Account(generatedKey, account.username, account.password);
            } else{
                result = null;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    public Account daoLogin (Account account) {
        Account result = null;
        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, account.username);
            ps.setString(2, account.password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                result = new Account(rs.getInt(1), rs.getString(2), rs.getString(3));
            } else {
                result = null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public Message daoCreateMessage(Message message){
        Message result = null;
        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?); ";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.posted_by);
            ps.setString(2, message.message_text);
            ps.setLong(3,message.time_posted_epoch);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()){
                message.setMessage_id(generatedKeys.getInt(1));
                result = message;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public Message updateMessage(Message message) {
        Message result = null;
        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?; ";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, message.message_text);
            ps.setInt(2, message.message_id);
            int rowsModified = ps.executeUpdate();
            if (rowsModified == 1){
                result = getMessage(message);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    private Message createNewMessageFromResultSet(ResultSet rs) throws SQLException{
        Message result = new Message();
        result.setMessage_id(rs.getInt(1));
        result.setPosted_by(rs.getInt(2));
        result.setMessage_text(rs.getString(3));
        result.setTime_posted_epoch(rs.getLong(4));
        return result;
    }

    public Message deleteMessage(Message message) {
        Message result = null;
        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "DELETE FROM message WHERE message_id = ? ; ";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.message_id);
            result = getMessage(message);
            if (result != null){
                int rowsModified = ps.executeUpdate();
                if (rowsModified != 1){
                    result = null;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public Message getMessage(Message message) {
        Message result = null;
        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message WHERE message_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message.message_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = createNewMessageFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public List<Message> getAllMessages(){
        List<Message> resultList= new ArrayList<>();
        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Message result = createNewMessageFromResultSet(rs);
                resultList.add(result);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return resultList;
    }

    public List<Message> getAllMessagesFromUser(Message message){
        List<Message> resultList= new ArrayList<>();
        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message WHERE posted_by = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message.posted_by);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Message result = createNewMessageFromResultSet(rs);
                resultList.add(result);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return resultList;
    }




}
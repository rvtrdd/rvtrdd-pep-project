package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.*;
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




}
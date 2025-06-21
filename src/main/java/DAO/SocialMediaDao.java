package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;

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




}
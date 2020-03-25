/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Product;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tvtien
 */
public class DataProcess {

    public Connection getConnection() {
        Connection conn = null;
        try {
            //Load Driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try {
                //Open connection
                conn = DriverManager.getConnection("jdbc:sqlserver://127.0.0.1:1433;databaseName=Demo1", "sa", "thaylinh");
            } catch (SQLException ex) {
                Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public ArrayList<Product> selectData() {
        ArrayList<Product> list = new ArrayList<>();
        try {
            //Statement sử dụng câu lệnh SQL tĩnh khi chạy
            Statement stmt = getConnection().createStatement();
            //Trả về một đối tượng ResultSet khi thực thi câu lệnh SELECT
            ResultSet rs = stmt.executeQuery("SELECT * FROM tblProduct");
            //add products vào ArrayList
            while (rs.next()) {
                Product product = new Product(rs.getString(1), rs.getString(2), rs.getFloat(3), rs.getInt(4));
                list.add(product);
            }
            rs.close();
            stmt.close();
            getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean insertData(Product product) {
        String sql = "INSERT INTO tblProduct VALUES(? , ?, ?, ?)";
        int res = 0;
        try {
            //PreparedStatement cho phép chỉ định tham số đầu vào khi chạy
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setString(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.setFloat(3, product.getPrice());
            stmt.setInt(4, product.getQuantity());
            //Trả về số dòng bị tác động khi thực thi các câu lệnh
            res = stmt.executeUpdate();
            stmt.close();
            getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res > 0;
    }

    public boolean checkExist(String id) {
        String sql = "SELECT * FROM tblProduct WHERE id = ?";
        boolean check = false;
        try {
            //PreparedStatement cho phép chỉ định tham số đầu vào khi chạy
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setString(1, id);
            //Trả về một đối tượng ResultSet khi thực thi câu lệnh SELECT
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (!(rs == null)) {
                    check = true;
                }
            }
            rs.close();
            stmt.close();
            getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return check;
    }

    public boolean updateData(Product product) {
        String sql = "UPDATE tblProduct SET name = ?, price = ?, quantity = ? WHERE id = ?";
        int res = 0;
        try {
            //PreparedStatement cho phép chỉ định tham số đầu vào khi chạy
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setString(4, product.getId());
            stmt.setString(1, product.getName());
            stmt.setFloat(2, product.getPrice());
            stmt.setInt(3, product.getQuantity());
            //Trả về số dòng bị tác động khi thực thi các câu lệnh
            res = stmt.executeUpdate();
            stmt.close();
            getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res > 0;
    }

    public boolean deleteData(String id) {
        String sql = "DELETE FROM tblProduct WHERE id = ?;";
        int res = 0;
        try {
            //PreparedStatement cho phép chỉ định tham số đầu vào khi chạy
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            stmt.setString(1, id);
            //Trả về số dòng bị tác động khi thực thi các câu lệnh
            res = stmt.executeUpdate();
            stmt.close();
            getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res > 0;
    }
}

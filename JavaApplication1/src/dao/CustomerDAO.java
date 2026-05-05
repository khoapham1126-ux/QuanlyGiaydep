package dao;

import model.Customer;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public boolean insert(Customer c) {
        String sql = "INSERT INTO Customer(FullName, Phone, Email, Address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getFullName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getAddress());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi insert Customer: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Customer c) {
        String sql = "UPDATE Customer SET FullName=?, Phone=?, Email=?, Address=? WHERE CustomerID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getFullName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getAddress());
            ps.setInt(5, c.getCustomerId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi update Customer: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Customer WHERE CustomerID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi delete Customer: " + e.getMessage());
            return false;
        }
    }

    public List<Customer> selectAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Customer(
                    rs.getInt("CustomerID"),
                    rs.getString("FullName"),
                    rs.getString("Phone"),
                    rs.getString("Email"),
                    rs.getString("Address")
                ));
            }
        } catch (Exception e) {
            System.out.println("Lỗi selectAll Customer: " + e.getMessage());
        }
        return list;
    }

    public List<Customer> searchByName(String keyword) {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer WHERE FullName LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Customer(
                        rs.getInt("CustomerID"),
                        rs.getString("FullName"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Address")
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi search Customer: " + e.getMessage());
        }
        return list;
    }
}
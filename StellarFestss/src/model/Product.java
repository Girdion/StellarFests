package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import util.Database;

public class Product {
    private String product_id;
    private String product_name;
    private String product_description;
    private String vendor_id;

    private Database connect;

    public Product() {
        connect = Database.getInstance();
    }

    public Product(String product_id, String product_name, String product_description, String vendor_id) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.vendor_id = vendor_id;
    }

    public String addProduct(String product_name, String product_descriptione, String vendor_id) {
        String validationMessage = validateProduct(product_name, product_description);
        if (!validationMessage.isEmpty()) {
            return validationMessage;
        }

        String query = "INSERT INTO products (product_id, product_name, product_description, product_price, vendor_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, generateProductId());
            ps.setString(2, product_name);
            ps.setString(3, product_description);
            ps.setString(4, vendor_id);
            ps.executeUpdate();
            return "Product added successfully";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error adding product";
        }
    }

    private String validateProduct(String product_name, String product_description) {
        if (product_name == null || product_name.trim().isEmpty()) {
            return "Product Name cannot be empty";
        }
        if (product_description == null || product_description.trim().isEmpty()) {
            return "Product Description cannot be empty";
        }
        if (product_description.length() > 200) {
            return "Product Description must be at most 200 characters long";
        }
        return "";
    }

    private String generateProductId() {
        String query = "SELECT product_id FROM products ORDER BY product_id DESC LIMIT 1";
        try (ResultSet rs = connect.execQuery(query)) {
            if (rs.next()) {
                String lastId = rs.getString("product_id");
                int num = Integer.parseInt(lastId.substring(1));
                return String.format("P%05d", num + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "P00001";
    }

    public Vector<Product> getProductsByVendor(String vendorId) {
        Vector<Product> products = new Vector<>();
        String query = "SELECT * FROM products WHERE vendor_id = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, vendorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getString("product_id"),
                        rs.getString("product_name"),
                        rs.getString("product_description"),
                        rs.getString("vendor_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public void updateProduct(String product_id, String product_name, String product_description, double product_price) {
        String query = "UPDATE products SET product_name = ?, product_description = ? WHERE product_id = ?";
        try (PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setString(1, product_name);
            ps.setString(2, product_description);
            ps.setString(3, product_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_description() {
        return product_description;
    }
    public String getVendor_id() {
        return vendor_id;
    }
}

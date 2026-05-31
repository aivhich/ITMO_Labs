package org.ivanrevich.managers;

import org.ivanrevich.models.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PSQLStorageManagerImpl implements StorageManager{
    String url = "jdbc:postgresql://localhost:5432/ivanrevich";
    String user = "ivanrevich";
    String password = "ivanrevich"; // TODO THAT's fucking shit, move it in configs file


    @Override
    public void save(ArrayList<Vehicle> queue, String path) {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("");
            while (rs.next()) {
                String name = rs.getString(1); // по номеру столбца
                int id = rs.getInt("id"); // по имени столбца
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Vehicle> load(String path) {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM vehicles");
            while (rs.next()) {
                String name = rs.getString(1); // по номеру столбца
                int id = rs.getInt("id"); // по имени столбца
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return List.of();
    }
}

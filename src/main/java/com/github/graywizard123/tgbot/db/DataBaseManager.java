package com.github.graywizard123.tgbot.db;

import com.github.graywizard123.tgbot.App;
import com.github.graywizard123.tgbot.db.models.CategoryModel;
import com.github.graywizard123.tgbot.db.models.MealModel;
import com.github.graywizard123.tgbot.db.models.OrderModel;
import com.github.graywizard123.tgbot.db.models.UserModel;
import com.github.graywizard123.tgbot.db.table.TableColumn;
import com.github.graywizard123.tgbot.db.table.TableUtils;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DataBaseManager {

    public static final String DATABASE_PATH = "//localhost/tgbot";
    public static final String CONFIG_FILE_PATH = "/database.properties";

    private static Connection connection;
    private static Statement statement;

    public static void init(){
        try {
            App.LOGGER.debug("Initializing database manager");
            Class.forName("org.postgresql.Driver");

            Properties props = new Properties();
            props.load(DataBaseManager.class.getResourceAsStream(CONFIG_FILE_PATH));
            connection = DriverManager.getConnection("jdbc:postgresql:"+DATABASE_PATH, props);
            statement = connection.createStatement();

            createTable(UserModel.getTableName(), UserModel.getTableColumns());
            createTable(MealModel.getTableName(), MealModel.getTableColumns());
            createTable(OrderModel.getTableName(), OrderModel.getTableColumns());
            createTable(CategoryModel.getTableName(), CategoryModel.getTableColumns());

            App.LOGGER.info("Database manager initialized");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(String name, TableColumn[] columns) {
        try {
            App.LOGGER.debug("Creating " + name);
            statement.execute("CREATE TABLE IF NOT EXISTS " +
                                  name +
                                  TableUtils.fromColumnArrayToSQL(columns) + ';');
            App.LOGGER.info(name + " was created");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static ResultSet executeQuery(String sql) throws SQLException{
        return statement.executeQuery(sql);
    }

    public static void executeUpdate(String sql) throws SQLException{
        statement.executeUpdate(sql);
    }

    public static void close() throws SQLException {
        connection.close();
    }

}

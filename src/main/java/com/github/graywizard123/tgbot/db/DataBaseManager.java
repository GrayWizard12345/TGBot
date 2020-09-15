package com.github.graywizard123.tgbot.db;

import com.github.graywizard123.tgbot.App;
import com.github.graywizard123.tgbot.db.models.Category;
import com.github.graywizard123.tgbot.db.models.Meal;
import com.github.graywizard123.tgbot.db.models.Order;
import com.github.graywizard123.tgbot.db.models.User;
import com.github.graywizard123.tgbot.db.table.TableColumn;
import com.github.graywizard123.tgbot.db.table.TableUtils;

import java.sql.*;

public class DataBaseManager {

    public static final String DATABASE_PATH = "database.sqlite";

    private static Connection connection;
    private static Statement statement;

    public static void init(){
        try {
            App.LOGGER.debug("Initializing database manager");
            connection = DriverManager.getConnection("jdbc:sqlite:"+DATABASE_PATH);
            statement = connection.createStatement();

            createTable(User.getTableName(), User.getTableColumns());
            createTable(Meal.getTableName(), Meal.getTableColumns());
            createTable(Order.getTableName(), Order.getTableColumns());
            createTable(Category.getTableName(), Category.getTableColumns());

            App.LOGGER.info("Database manager initialized");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.exit(1);
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

}

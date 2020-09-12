package com.github.graywizard123.tgbot.db;

import com.github.graywizard123.tgbot.App;
import com.github.graywizard123.tgbot.db.models.IModel;
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
            App.LOGGER.info("Database manager initialized");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.exit(1);
        }
    }

    public static void registerModel(IModel model){
        try {
            App.LOGGER.debug("Registering " + model.getTableName());
            statement.execute("CREATE TABLE IF NOT EXISTS " +
                                  model.getTableName() +
                                  TableUtils.fromModelToSQL(model) + ';');
            App.LOGGER.info(model.getTableName() + " was registered");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static ResultSet executeQuery(String sql) throws SQLException{
        return statement.executeQuery(sql);
    }

    public static void executeUpdate(String sql) throws SQLException{
        statement.executeUpdate(sql);
        connection.commit();
    }

}

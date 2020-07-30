package org.example.test29.sql.low;

import org.example.test29.sql.SqlQuery;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class SqlQueryMsSql implements SqlQuery {
    private String fileNamePropertys;
    private SqlQuery.Parameters parameters;
    private SqlQuery delegate;
    private Connection connection;

    public SqlQueryMsSql(String fileNamePropertys, SqlQuery delegate) {
        this.fileNamePropertys = fileNamePropertys;
        this.delegate = delegate;

        parameters = new SqlQuery.Parameters();
        loadParametersSql();
        connectSql();

    }

    private void loadParametersSql() {
        Properties properties = new Properties();
        try {
            properties.load(new BufferedReader(new FileReader(fileNamePropertys)));
            if (parameters.get(properties)) {
                delegate.setDefaultParametrsSql(parameters);
                savePropertys();
            }
            return;
        } catch (IOException e) {
            //e.printStackTrace(); // файл отсутствует
            System.out.println("файл с параметрами подключения не найден");
        }
        delegate.setDefaultParametrsSql(parameters);
        parameters.set(properties);
        savePropertys();
    }

    private void savePropertys() {
        Properties properties = new Properties();
        parameters.set(properties);
        try {
            properties.store(new BufferedWriter(new FileWriter(fileNamePropertys)), "access sql");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectSql() {
        String connectionUrl = "jdbc:sqlserver://%1$s:%2$s;databaseName=%3$s;user=%4$s;password=%5$s;";
        String connectionString = String.format(connectionUrl
                , parameters.urlServer
                , parameters.portServer
                , parameters.database
                , parameters.user
                , parameters.password);

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            System.out.println("ошибка инициализации драйвера");
            System.exit(1);
        }

        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            //ex.printStackTrace();
            System.out.println("ошибка подключения к серверу");
            System.exit(1);
        }
    }

    @Override
    public void setDefaultParametrsSql(Parameters parametrsSql) {
    }

    public ResultSet executeQuery(String stringQueue) {
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(stringQueue);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Class<?> dd = null;

        return resultSet;
    }

    @Override
    public void executeUpdate() {

    }


}

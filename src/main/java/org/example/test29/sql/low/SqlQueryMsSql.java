package org.example.test29.sql.low;

import org.example.test29.sql.MyBlob;
import org.example.test29.sql.SqlQuery;

import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.function.Consumer;

public class SqlQueryMsSql implements SqlQuery {
    private String fileNamePropertys;
    private SqlQuery.Parameters parameters;
    private Consumer callBackSetDefaultParameters;
    private Connection connection;

    public SqlQueryMsSql(String fileNamePropertys, Consumer callBackSetDefaultParameters) {
        this.fileNamePropertys = fileNamePropertys;
        this.callBackSetDefaultParameters = callBackSetDefaultParameters;

        parameters = new SqlQuery.Parameters();
        loadParametersSql();
        connectSql();

    }

    private void loadParametersSql() {
        Properties properties = new Properties();
        try {
            properties.load(new BufferedReader(new FileReader(fileNamePropertys)));
            if (parameters.get(properties)) {
                callBackSetDefaultParameters.accept(parameters);
                savePropertys();
            }
            return;
        } catch (IOException e) {
            //e.printStackTrace(); // файл отсутствует
            System.out.println("файл с параметрами подключения не найден");
        }
        callBackSetDefaultParameters.accept(parameters);
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
    public void executeUpdate(String tab, String[] fields, Object[] data, Class[] classData) {
        try {
            //String pareStatement = "INSERT INTO Table_1(id_spec, ves, dis) VALUES (?, ?, ?)";
            String pareStatement = "INSERT INTO " + tab + "(" + fields[0];
            String pareStatementValue = ") VALUES (?";
            for (int i = 1; i < fields.length; i++) {
                pareStatement += ", " + fields[i];
                pareStatementValue += ", ?";
            }
            pareStatement += pareStatementValue + ")";
            PreparedStatement statement = connection.prepareStatement(pareStatement);

            for (int i = 0; i < data.length; i++) {
                String[] strings = classData[i].getName().split("\\.");
                switch (classData[i].getName()) {
                    case "int":
                        statement.setInt(i + 1, (int) data[i]);
                        break;
                    case "byte":
                        statement.setByte(i + 1, (byte) data[i]);
                        break;
                    case "[B":
                        statement.setBytes(i + 1, (byte[]) data[i]);
                        break;
                    case "MyBlob":
                        statement.setBlob(i + 1, (MyBlob) data[i]);
                        break;
                    case "Timestamp":
                        statement.setTimestamp(i + 1, (Timestamp) data[i] );
                        break;

                }
            }
            statement.executeUpdate();
            statement.close();

        } catch (java.lang.Throwable e) {
            e.printStackTrace();
        }
    }


}

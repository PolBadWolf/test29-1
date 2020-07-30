package org.example.test29;

import org.example.test29.sql.MyBlob;
import org.example.test29.sql.SqlQuery;
import org.example.test29.sql.low.SqlQueryMsSql;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.function.Consumer;

public class MainClass implements SqlQuery {

    private SqlQuery sqlQuery = null;

    public static void main(String[] args) {
        new Dop().start();
        new MainClass().start();
    }

    private void defaultProperties(Properties properties, String fileName) {
        properties.clear();
        properties.setProperty("URL_Server", "127.0.0.1" );
        properties.setProperty("Port_Server", "1433");
        properties.setProperty("DataBase", "spc1");
        properties.setProperty("User", "max");
        properties.setProperty("Password", "1122");
        try {
            properties.store(new BufferedWriter(new FileWriter(fileName)), "access base");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void start() {
        System.out.println("start programm");

        sqlQuery = new SqlQueryMsSql("z1.txt", this);

        Properties properties = new Properties();
        String connectionString;
        Connection connection = null;

        // загрузка
        try {
            properties.load(new BufferedReader(new FileReader("z1.txt")));
        } catch (IOException e) {
            // файл отсутствует
            defaultProperties(properties, "z1.txt");
        }

        {
            String URL_Server = properties.getProperty("URL_Server");
            String Port_Server = properties.getProperty("Port_Server");
            String DataBase = properties.getProperty("DataBase");
            String User = properties.getProperty("User");
            String Password = properties.getProperty("Password");
            if ((URL_Server == null)
                    || (Port_Server == null)
                    || (DataBase == null)
                    || (User == null)
                    || (Password == null)) {
                defaultProperties(properties, "z1.txt");
                URL_Server = properties.getProperty("URL_Server");
                Port_Server = properties.getProperty("Port_Server");
                DataBase = properties.getProperty("DataBase");
                User = properties.getProperty("User");
                Password = properties.getProperty("Password");
            }
            String connectionUrl = "jdbc:sqlserver://%1$s:%2$s;databaseName=%3$s;user=%4$s;password=%5$s;";
            connectionString = String.format(connectionUrl
                    , URL_Server
                    , Port_Server
                    , DataBase
                    , User
                    , Password);
        }

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

        try {
            PreparedStatement statement;

            ArrayList<Ddt> tt = new ArrayList<>();
            for (int i = 0; i < 721_001; i++) {
                tt.add(new Ddt(i, i));
            }
            byte[] q = Ddt.toBytes( tt.toArray(new Ddt[tt.size()]));
            Blob blob = new MyBlob(q);

            statement = connection.prepareStatement("INSERT INTO Table_1(id_spec, ves, dis) VALUES (?, ?, ?)");
            statement.setInt(1, 66);
            statement.setInt(2, 77);
            statement.setBlob(3, blob);

            statement.executeUpdate();
            statement.close();

        } catch (java.lang.Throwable e) {
            e.printStackTrace();
        }

        try {
            Statement stmt = connection.createStatement();
            ResultSet executeQuery = stmt.executeQuery("SELECT * FROM spc1.dbo.Table_1");

            while (executeQuery.next()) {
                int id_spec = executeQuery.getInt("id_spec");
                int ves = executeQuery.getInt("ves");
                Blob blob = executeQuery.getBlob("dis");

                System.out.print(id_spec + "\t" + ves);
                if (blob != null) {
                    System.out.print("\tdis len = " + blob.length() / 6);
                }
                System.out.println();
            }

            executeQuery.close();
            stmt.close();
        } catch (java.lang.Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setDefaultParametrsSql(Parameters parametrsSql) {
        parametrsSql.urlServer = "";
    }

    @Override
    public ResultSet executeQuery(String stringQueue) {
        return null;
    }

    @Override
    public void executeUpdate() {

    }
}

package org.example.test29;

import java.sql.*;
import java.util.ArrayList;

public class MainClass {

    public static void main(String[] args) {
        new MainClass().start();
    }

    private void start() {
        System.out.println("start programm");

        try {
//            String instanceName = "127.0.0.1\\SQLEX";
            String instanceName = "127.0.0.1\\SQLEX:1433";
            String databaseName = "spc1";
            String userName = "max";
            String pass = "1122";
            String connectionUrl = "jdbc:sqlserver://%1$s;databaseName=%2$s;user=%3$s;password=%4$s;";
            String connectionString = String.format(connectionUrl, instanceName, databaseName, userName, pass);

            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Connection connection = DriverManager.getConnection(connectionString);

            Statement stmt = connection.createStatement();

            ResultSet executeQuery = stmt.executeQuery("SELECT * FROM spc1.dbo.Table_1");

            while (executeQuery.next()) {
                int id_spec = executeQuery.getInt("id_spec");
                int ves = executeQuery.getInt("ves");
                Blob blob = executeQuery.getBlob("dis");

                System.out.print(id_spec + "\t" + ves);
                if (blob != null) {
                    System.out.print("\tdis len = " + blob.length());
                }
                System.out.println();
            }

            executeQuery.close();
            stmt.close();

            PreparedStatement statement = connection.prepareStatement("INSERT INTO Table_1(id_spec, ves, dis) VALUES (?, ?, ?)");
            statement.setInt(1, 66);
            statement.setInt(2, 77);

            ArrayList<Ddt> tt = new ArrayList<>();
            for (int i = 1; i < 200_001; i++) {
                tt.add(new Ddt(i, i));
            }

            byte[] q = Ddt.toBytes( tt.toArray(new Ddt[tt.size()]));

            Blob blob = new javax.sql.rowset.serial.SerialBlob(q);

            statement.setBlob(3, blob);

            statement.executeUpdate();
            statement.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

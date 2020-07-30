package org.example.test29.sql;

import java.sql.ResultSet;
import java.util.Properties;

public interface SqlQuery {
    void setDefaultParametrsSql(Parameters parametrsSql);
    ResultSet executeQuery(String stringQueue);
    void executeUpdate();

    class Parameters {
        public String urlServer;
        public String portServer;
        public String database;
        public String user;
        public String password;

        public boolean get(Properties properties) {
            urlServer = properties.getProperty("URL_Server");
            portServer = properties.getProperty("Port_Server");
            database = properties.getProperty("DataBase");
            user = properties.getProperty("User");
            password = properties.getProperty("Password");
            return  (  (urlServer == null)
                    || (portServer == null)
                    || (database == null)
                    || (user == null)
                    || (password == null)
            );
        }

        public void set(Properties properties) {
            properties.clear();
            properties.setProperty("URL_Server", urlServer);
            properties.setProperty("Port_Server", portServer);
            properties.setProperty("DataBase", database);
            properties.setProperty("User", user);
            properties.setProperty("Password", password);
        }
    }
}

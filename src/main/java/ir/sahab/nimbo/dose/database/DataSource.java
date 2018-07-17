package ir.sahab.nimbo.dose.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import ir.sahab.nimbo.dose.Config;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static DataSource dataSource;
    private ComboPooledDataSource cpds;

    private DataSource() throws IOException, SQLException, PropertyVetoException {
        cpds = new ComboPooledDataSource();

        cpds.setDriverClass("com.mysql.cj.jdbc.Driver");
        cpds.setJdbcUrl(Config.getInstance().SQL_URL + Config.getInstance().DB_NAME + "?" +
                Config.getInstance().CONNECTION_SETTINGS);
        cpds.setUser(Config.getInstance().USER);
        cpds.setPassword(Config.getInstance().PASS);


        // the settings below are optional -- c3p0 can work with defaults
        // TODO: set these in config file
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        cpds.setMaxStatements(180);

    }

    public static DataSource getInstance() throws IOException, SQLException, PropertyVetoException {
        if (dataSource == null) {
            dataSource = new DataSource();
            return dataSource;
        } else {
            return dataSource;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }

}
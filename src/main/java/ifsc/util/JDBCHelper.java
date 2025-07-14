package ifsc.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCHelper {

    private static final Properties properties = new Properties();
    private static Connection connection = null;

    static {
        try (InputStream input = JDBCHelper.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.err.println("Arquivo application.properties não encontrado no classpath.");
                throw new IOException("Arquivo de propriedades não encontrado");
            }
            properties.load(input);
            Class.forName("org.postgresql.Driver");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Falha ao carregar configuração do banco de dados.", e);
        }
    }

    private JDBCHelper() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password")
            );
        }
        return connection;
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
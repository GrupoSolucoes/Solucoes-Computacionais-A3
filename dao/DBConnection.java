package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe responsável pela conexão com o banco de dados MySQL.
 * As credenciais são lidas de um arquivo config.properties.
 */
public class DBConnection {
    private static final Properties properties = new Properties();

    // Bloco estático para carregar as propriedades uma única vez quando a classe é carregada
    static {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Desculpe, não foi possível encontrar o arquivo config.properties");
            } else {
                // Carrega o arquivo de propriedades
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        // Usa as propriedades carregadas do arquivo para criar a conexão
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }
}
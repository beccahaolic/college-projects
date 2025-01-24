package com.iotproject.config;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Componente que testa a conexão com o banco de dados durante a inicialização da aplicação.
 * Exibe informações da conexão, como URL e nome de usuário, ou imprime um erro em caso de falha.
 */
@Component
public class DatabaseConnectionTest implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    /**
     * Executa o teste de conexão com o banco de dados ao iniciar a aplicação.
     *
     * @param args Argumentos passados na linha de comando (não utilizados neste contexto).
     * @throws Exception Em caso de falhas inesperadas durante a execução.
     */
    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Conexão com o banco de dados foi bem-sucedida!");
            System.out.println("URL: " + connection.getMetaData().getURL());
            System.out.println("Usuário: " + connection.getMetaData().getUserName());
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }
}

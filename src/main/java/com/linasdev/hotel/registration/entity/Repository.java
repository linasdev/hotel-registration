package com.linasdev.hotel.registration.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Repository implements AutoCloseable {
    private final Connection connection;

    public Repository(String pathToDatabase) throws Exception {
        boolean shouldSeed = !new File(pathToDatabase).exists();

        this.connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", pathToDatabase));
        this.connection.setAutoCommit(true);
        migrate();

        if (shouldSeed) {
            seed();
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return this.connection.prepareStatement(sql);
    }

    public Statement createStatement() throws SQLException {
        return this.connection.createStatement();
    }

    private void migrate() throws Exception {
        migrateFolder("migration");
    }

    private void seed() throws Exception {
        migrateFolder("seed");
    }

    private void migrateFolder(String folderName) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL migrationFolderUrl = classloader.getResource(folderName);

        if (migrationFolderUrl == null) {
            return;
        }

        File migrationFolder = new File(migrationFolderUrl.toURI());
        File[] migrationFiles = migrationFolder.listFiles();

        if (migrationFiles == null) {
            return;
        }

        for (File file : migrationFiles) {
            System.err.printf("Migrating database to version '%s'.%n", file.getName());

            try (InputStream migrationFile = new FileInputStream(file)) {
                try (Statement statement = createStatement()) {
                    statement.execute(new String(migrationFile.readAllBytes()));
                }
            }
        }
    }
}

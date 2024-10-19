package org.example;

import java.sql.Connection;

public class DatabaseOperation implements Runnable {
    private final ConnectionPool connectionPool;

    DatabaseOperation(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void run() {
        try {
            Connection conn = connectionPool.getDBConnection();
            Thread.sleep(1000);
            connectionPool.addConnectionBackToPool(conn);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

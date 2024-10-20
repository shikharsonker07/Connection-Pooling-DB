package org.example;

import java.sql.Connection;
import java.util.concurrent.BlockingDeque;

public class ConnectionPool {
    private final BlockingDeque<Connection> pool;

    ConnectionPool(BlockingDeque<Connection> pool) {
        this.pool = pool;
    }

    public Connection getDBConnection() {
        try {
            return pool.take();
//            System.out.println(Thread.currentThread().getName() + " connected to database, performing DB operations");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void addConnectionBackToPool(Connection conn) {
        try {
//            System.out.println(Thread.currentThread().getName() + " completed DB task");
            pool.put(conn);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

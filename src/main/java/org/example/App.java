package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
import java.util.concurrent.*;


public class App {
    private static int MAX_CONNECTIONS;
    private static ExecutorService executorService;
    private static ConnectionPool connectionPool;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Size of Connection Pool");
        int poolSize = sc.nextInt();
        System.out.println("Max concurrent connections to the DB");
        MAX_CONNECTIONS = sc.nextInt();
        System.out.println("No of Concurrent Requests on Server");
        int concurrentRequests = sc.nextInt();

        initializeConnectionPool(poolSize);

        System.out.println("Time taken for Requests using Connection Pooling ");
        System.out.println("----------------------------------------------------");
        concurrentRequestsWithPool(concurrentRequests);


        System.out.println("Time taken for Requests without Pool");
        System.out.println("----------------------------------------------------");
        concurrentRequests(concurrentRequests);
    }

    public static void concurrentRequestsWithPool(int concurrentRequests) {
        executorService = Executors.newFixedThreadPool(concurrentRequests);
        Instant startTime = Instant.now();

        for (int i = 0; i < concurrentRequests; i++) {
            executorService.submit(() -> {
                try {
                    Connection conn = connectionPool.getDBConnection();
                    Thread.sleep(1000);
                    connectionPool.addConnectionBackToPool(conn);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        executionTime(startTime);
    }

    public static void concurrentRequests(int concurrentRequests) {
        Semaphore semaphore = new Semaphore(MAX_CONNECTIONS);
        executorService = Executors.newFixedThreadPool(concurrentRequests);

        Instant startTime = Instant.now();

        for (int i = 0; i < concurrentRequests; i++) {
            executorService.submit(() -> {
                try {
                    semaphore.acquire();
//                    System.out.println(Thread.currentThread().getName() + " connected to DB, performing DB operations");
                    Connection conn = DatabaseConnection.getConnection();
                    // Database Operations
                    Thread.sleep(1000);
                    conn.close();
//                    System.out.println(Thread.currentThread().getName() + " completed DB task");
                } catch (SQLException | InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    semaphore.release();
                }
            });
        }

        executionTime(startTime);
    }

    private static void executionTime(Instant startTime) {
        executorService.shutdown();
        try {
            // Wait indefinitely for all tasks to complete
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for tasks to complete", e);
        }


        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        System.out.println("----------------------------------------------------");
        System.out.println("Total milliseconds " + duration.toMillis());
        System.out.println("----------------------------------------------------");
        System.out.println('\n');
    }

    private static void initializeConnectionPool(int poolSize) {
        BlockingDeque<Connection> pool = new LinkedBlockingDeque<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            try {
                pool.put(DatabaseConnection.getConnection());
            } catch (InterruptedException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
        connectionPool = new ConnectionPool(pool);
    }
}

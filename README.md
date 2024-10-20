
# Connection Pool for DBs using Blocking Queue


Implemented connection pooling for concurrent Requests on a server connecting to database simultaneously. Used,
1. Postgres Docker Image to connect to DB locally
2. Mimicked concurrent requests on server, using Threads
3. Benchmarked numbers With/Without connection Pool


## Installation

1. **Clone the repository:**

    ```bash
    git clone https://github.com/shikharsonker07/Connection-Pooling-DB.git
    ```

2. **Install maven dependencies**

    ```bash
      mvn clean install
    ```
3. **Update DB connection config**
   ```bash
   in DatabaseConnection.java class (can also use H2 in-memory DB)
    ```

## Running the Program
    Run the main method in App.java class
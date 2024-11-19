import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class DBManager {
    String url;
    String userName;
    String password;
    String DatabaseName;

    public DBManager(String userName, String password, String databaseName) {
        this.url = "jdbc:mysql://localhost:3306/";
        this.userName = userName;
        this.password = password;
        DatabaseName = databaseName;
    }

    public boolean isDB(){
        boolean isDatabase = false;
        try( Connection connection = DriverManager.getConnection(url,userName,password) ){
             DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getCatalogs();
            while (resultSet.next()){
              String catalog  = resultSet.getString(1);
              if(catalog.equalsIgnoreCase(getDatabaseName())){
                  isDatabase = true;
                  break;
              }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return isDatabase;
    }

    public boolean isTables(){
        setUrl("jdbc:mysql://localhost:3306/"+getDatabaseName());
        boolean isTable = false;
        String sqlCheckTables = """
                SHOW TABLES;
                """;
        try( Connection connection = DriverManager.getConnection(url,userName,password);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlCheckTables);
        ){
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> dataBaseTabes = new ArrayList<>();
            while (resultSet.next()){
                String name = resultSet.getString(1);
                dataBaseTabes.add(name);
            }
            isTable = requiredTables(dataBaseTabes);

        } catch (SQLException e){
            e.printStackTrace();
        }
        return isTable;
    }

    public void createTables() {
        setUrl("jdbc:mysql://localhost:3306/"+getDatabaseName());

        String sqlTeacherTable = """
    CREATE TABLE Teacher (
        id VARCHAR(50) PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        lastName VARCHAR(100) NOT NULL,
        gender VARCHAR(10) NOT NULL,
        age INT NOT NULL,
        salary DOUBLE NOT NULL
    );
    """;

        String sqlStudentTable = """
    CREATE TABLE Student (
        id VARCHAR(50) PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        lastName VARCHAR(100) NOT NULL,
        gender VARCHAR(10) NOT NULL,
        age INT NOT NULL
    );
    """;

        String sqlCourseTable = """
    CREATE TABLE Course (
        id VARCHAR(50) PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        maxPoints DOUBLE NOT NULL
    );
    """;

        String sqlEnrollmentStudentTable = """
    CREATE TABLE EnrollmentST (
        id VARCHAR(50) PRIMARY KEY,
        studentId VARCHAR(50),
        teacherId VARCHAR(50),
        courseId VARCHAR(50),
        marks DOUBLE,
        FOREIGN KEY (studentId) REFERENCES Student(id),
        FOREIGN KEY (teacherId) REFERENCES Teacher(id),
        FOREIGN KEY (courseId) REFERENCES Course(id)
    );
    """;

        String sqlEnrollmentTeacherTable = """
    CREATE TABLE EnrollmentTC (
        id VARCHAR(50) PRIMARY KEY,
        courseId VARCHAR(50),
        teacherId VARCHAR(50),
        FOREIGN KEY (courseId) REFERENCES Course(id),
        FOREIGN KEY (teacherId) REFERENCES Teacher(id)
    );
    """;
        if(isDB()){
            try (Connection connection = DriverManager.getConnection(url, userName, password)) {
                connection.setAutoCommit(false); // Begin transaction

                try (Statement statement = connection.createStatement()) {
                    // Execute table creation statements using Statement
                    statement.executeUpdate(sqlTeacherTable);
                    statement.executeUpdate(sqlStudentTable);
                    statement.executeUpdate(sqlCourseTable);
                    statement.executeUpdate(sqlEnrollmentStudentTable);
                    statement.executeUpdate(sqlEnrollmentTeacherTable);

                    connection.commit(); // Commit transaction
                    System.out.println("Tables OK ✅");

                } catch (SQLException e) {
                    connection.rollback(); // Roll back if any table creation fails
                    System.err.println("Error creating tables: " + e.getMessage());
                }

            } catch (SQLException e) {
                System.err.println("Database connection failed: " + e.getMessage());
            }
        }else{
            createDatabase();
        }

    }

    public void createDatabase(){
        try(Connection connection = DriverManager.getConnection(url,userName,password)) {

            String sqlCreateDB = "CREATE DATABASE "+getDatabaseName()+";";

            try(Statement createDataBase = connection.createStatement()){
                createDataBase.executeUpdate(sqlCreateDB);
                setUrl("jdbc:mysql://localhost:3306/"+getDatabaseName());
                System.out.println("Database OK ✅");
            } catch (SQLException e){
                e.printStackTrace();
            }


        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean connectDBTechnician(){
        System.out.println("Integrity check 🛡 ⚖");
        boolean isConnected = false;
        if(!isDB()){
            System.out.println("creating DataBase [ "+getDatabaseName()+" ]");
            delayer(" createDatabase() ==> DBInfo");
            createDatabase();
            System.out.println("creating Tables [ Student, Teacher, EnrollmentST, EnrollmentTC ]");
            delayer(" createTables() ==> DBInfo");
            createTables();
            isConnected = true;
            System.out.println("Database Ready For Use ✅👍");
        }else{
            if(isTables()){
                delayer(" isTable() ==> DBInfo");
                System.out.println("Database Ready For Use ✅👍");
                isConnected = true;
            }else{
                delayer(" createTables() ==> DBInFo ");
                System.out.println("creating Tables [ Student, Teacher, EnrollmentST, EnrollmentTC ]");
                createTables();
                if(isTables()){
                    isConnected = true;
                    System.out.println("Database Ready For Use ✅👍");
                }

            }
        }
    return isConnected;
    }


    public void delayer(String process){
        int totalSteps = 10;  // Total steps for the progress bar
        for(int i = 0; i <= totalSteps; i++){
            try {
                Thread.sleep(500);  // Wait for 500 milliseconds before each step

                // Calculate the percentage
                int percentage = (int) ((double) (i) / totalSteps * 100);

                // Clear the current line and print the percentage
                System.out.print("\r[" + percentage + "%]"+" process ==> "+process);

                System.out.flush();  // Ensure the output is immediately written to the console
            } catch (InterruptedException e){
                System.err.println("Time interrupted");
            }
        }
        System.out.println();  // Print a newline after the progress bar is finished
    }


    public boolean requiredTables(ArrayList<String> presentTables){
        ArrayList<String> requiredTable = new ArrayList<>(Arrays.asList("Student","Teacher","EnrollmentST",
                "EnrollmentTC"));

        requiredTable.replaceAll(String::toLowerCase);
        presentTables.replaceAll(String::toLowerCase);

        return presentTables.containsAll(requiredTable);
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabaseName() {
        return DatabaseName;
    }

    public void setDatabaseName(String databaseName) {
        DatabaseName = databaseName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DBManager dbInfo = (DBManager) o;
        return Objects.equals(url, dbInfo.url) && Objects.equals(userName, dbInfo.userName) && Objects.equals(password, dbInfo.password) && Objects.equals(DatabaseName, dbInfo.DatabaseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, userName, password, DatabaseName);
    }
}

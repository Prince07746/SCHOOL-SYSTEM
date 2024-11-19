package DBOperations;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class DBManager {
    String urlNoDBName;
    String url;
    String customUrl;
    String userName;
    String password;
    String DatabaseName;

    public DBManager(String userName, String password,String url,String databaseName) {
        this.url = url.isBlank() ? "jdbc:mysql://localhost:3306/": url;
        this.userName = userName;
        this.password = password;
        DatabaseName = databaseName;
        urlNoDBName = getUrl().substring(0,getUrl().lastIndexOf("/") + 1);

    }

    public DBManager(String userName, String password,String url) {
        this.url = url.isBlank() ? "jdbc:mysql://localhost:3306/": url;
        this.userName = userName;
        this.password = password;
        urlNoDBName = getUrl().substring(0,getUrl().lastIndexOf("/") + 1);

    }

    // =================== PRE-CONNECTION ===============================================================
    public boolean isDB(){
        boolean isDatabase = false;
        try( Connection connection = DriverManager.getConnection(urlNoDBName,userName,password) ){
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
            System.out.println("error ❌: "+e.getMessage());
        }
        return isDatabase;
    }

    public boolean isDBbyName(String DBName){
        delayer(" process isDBbyName() ");
        boolean isDatabase = false;
        try( Connection connection = DriverManager.getConnection(urlNoDBName+DBName,userName,password) ){
            isDatabase = true;
        } catch (SQLException e){
            System.out.println("error ❌: "+e.getMessage());
        }
        return isDatabase;
    }


    public void microDatabase(ArrayList<String> DataBaseNames){

        try(Connection connection = DriverManager.getConnection(urlNoDBName,userName,password)){
            connection.setAutoCommit(false);
            try(Statement statement = connection.createStatement();){

                for(String nameDB:DataBaseNames){
                    if(!isDBbyName(nameDB)){
                        String sqlCreateDataBase = "CREATE DATABASE "+nameDB+";";
                        statement.executeUpdate(sqlCreateDataBase);
                        System.out.println("DataBase [ "+nameDB+" ] OK ✅");
                        setUrl(urlNoDBName+nameDB);
                        setDatabaseName(nameDB);
                        createTables();
                        System.out.println("************************");
                    }else{
                        System.out.println(" [ "+nameDB+" ] exist ❌");
                    }
                }
                connection.commit();

            } catch (SQLException e){
                connection.rollback();
                System.out.println("Error creating Database [ MicroDataBase ] ");
            }

        } catch (SQLException e){
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
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
        System.out.println("Integrity check 🛡 ⚖ [ "+getDatabaseName()+" ]");
        boolean isConnected = false;
        if(!isDB()){
            System.out.println("creating DataBase [ "+getDatabaseName()+" ]");
            delayer(" createDatabase() ==> DBInfo");
            createDatabase();
            System.out.println("creating Tables [ Student, Teacher, EnrollmentST, EnrollmentTC ]");
            delayer(" createTables() ==> DBInfo");
            createTables();
            isConnected = true;
            System.out.println("Database [ "+getDatabaseName()+" ] Ready For Use ✅👍");
        }else{
            if(isTables()){
                delayer(" isTable() ==> DBInfo");
                System.out.println("Database [ "+getDatabaseName()+" ] Ready For Use ✅👍");
                isConnected = true;
            }else{
                delayer(" createTables() ==> DBInFo ");
                System.out.println("creating Tables [ Student, Teacher, EnrollmentST, EnrollmentTC ]");
                createTables();
                if(isTables()){
                    isConnected = true;
                    System.out.println("Database [ "+getDatabaseName()+" ] Ready For Use ✅👍");
                }

            }
        }
        System.out.println("--------------------------------------------------------------");
    return isConnected;
    }

    public void delayer(String process){
        int totalSteps = 10;  // Total steps for the progress bar
        for(int i = 0; i <= totalSteps; i++){
            try {
                Thread.sleep(300);  // Wait for 500 milliseconds before each step

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

    public void deleteDatabase(){
        if(isDB()){
            try(Connection connection = DriverManager.getConnection(url,userName,password);){
                String sqlDropDatabase = "DROP DATABASE "+getDatabaseName()+";";
                Statement statement = connection.createStatement();
                statement.executeUpdate(sqlDropDatabase);
                System.out.println("DATABASE "+getDatabaseName()+" Deleted ✅");
                setUrl("jdbc:mysql://localhost:3306/");
            } catch (SQLException e){
                System.out.println("error ❌"+e.getMessage());
            }
        }

    }

    public void renameDatabase(String newName){
        deleteDatabase();
        setDatabaseName(newName);
        if(connectDBTechnician()) System.out.println("DataBase rebooted [ new name ==> "+newName+" ] ✅");
        else System.out.println("Failed to Rename DataBase ❌");
    }


    // ====================================================================================================




    // ============== AFTER-CONNECTION ====================================================================



    // =====================================================================================================


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        this.urlNoDBName = url.substring(0, url.lastIndexOf("/") + 1);
    }

    public String getUrlNoDBName() {
        return urlNoDBName;
    }

    public void setUrlNoDBName(String urlNoDBName) {
        this.urlNoDBName = urlNoDBName;
    }

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
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

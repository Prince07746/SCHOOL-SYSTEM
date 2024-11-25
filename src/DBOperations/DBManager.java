package DBOperations;

import DBOperations.SqlTableUI.*;
import com.mysql.jdbc.Driver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;


public class DBManager implements DbOperationInterface {
    String urlNoDBName;
    String url;
    String customUrl;
    String userName;
    String password;
    String DatabaseName;

    static String fileTables = "src/DBOperations/DatabaseResource/table.csv";
    static String fileTablesCreateQuery = "src/DBOperations/DatabaseResource/tablePattern.csv";


    public DBManager(String userName, String password, String url, String databaseName) {
        this.url = url.isBlank() ? "jdbc:mysql://localhost:3306/" : url;
        this.userName = userName;
        this.password = password;
        DatabaseName = databaseName;
        urlNoDBName = getUrl().substring(0, getUrl().lastIndexOf("/") + 1);

    }

    public DBManager(String userName, String password, String url) {
        this.url = url.isBlank() ? "jdbc:mysql://localhost:3306/" : url;
        this.userName = userName;
        this.password = password;
        urlNoDBName = getUrl().substring(0, getUrl().lastIndexOf("/") + 1);
        this.DatabaseName = "";
    }

    // =================== PRE-CONNECTION  DDL ===============================================================
    @Override
    public boolean isDB() {
        boolean isDatabase = false;
        try (Connection connection = DriverManager.getConnection(urlNoDBName, userName, password)) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getCatalogs();
            while (resultSet.next()) {
                String catalog = resultSet.getString(1);
                if (catalog.equalsIgnoreCase(getDatabaseName())) {
                    isDatabase = true;
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println("error ❌: " + e.getMessage());
        }
        return isDatabase;
    }

    @Override
    public void isDataBaseBlankFixing() {
        StringBuilder dbNameFix = new StringBuilder();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Database Name: ");
        String dbName = scanner.nextLine();
        System.out.print("do you want to continue (Y/N): ");
        String confirmToContinue = scanner.nextLine();
        if ((getDatabaseName().isBlank()) && confirmToContinue.equalsIgnoreCase("Y")) {
            setDatabaseName(dbName);
            dbNameFix.append(getDatabaseName());
        }
    }

    // it checks if the Database exist
    @Override
    public boolean isDBbyName(String DBName) {
        delayer(" process isDBbyName() ");
        boolean isDatabase = false;
        try (Connection connection = DriverManager.getConnection(urlNoDBName + DBName, userName, password)) {
            isDatabase = true;
        } catch (SQLException e) {
            System.out.println("error ❌: " + e.getMessage());
        }
        return isDatabase;
    }

    // create database
    @Override
    public void createDatabase() {


        if (getDatabaseName().isEmpty()) isDataBaseBlankFixing();

        try (Connection connection = DriverManager.getConnection(url, userName, password)) {

            String sqlCreateDB = "CREATE DATABASE " + getDatabaseName() + ";";

            try (Statement createDataBase = connection.createStatement()) {
                createDataBase.executeUpdate(sqlCreateDB);
                setUrl("jdbc:mysql://localhost:3306/" + getDatabaseName());
                System.out.println("Database OK ✅");
            } catch (SQLException e) {
                e.printStackTrace();
            }


        } catch (SQLException e) {
            e.printStackTrace();

        }


    }

    // perform the integrity check (database ,tables , url)
    @Override
    public boolean connectDBTechnician() {
        System.out.println("Integrity check 🛡 ⚖ [ " + getDatabaseName() + " ]");
        boolean isConnected = false;
        Scanner scanner = new Scanner(System.in);

        if (!isDB()) {


            System.out.println("creating DataBase [ " + getDatabaseName() + " ]");
            delayer(" createDatabase() ==> DBInfo");
            createDatabase(); // create database
            delayer(" createTables() ==> DBInfo");
            createTables(); // create tables
            isConnected = true;
            if (isDB()) System.out.println("Database [ " + getDatabaseName() + " ] Ready For Use ✅👍");


        } else {
            if (isTables()) {

                delayer(" isTable() ==> DBInfo");
                System.out.println("Database [ " + getDatabaseName() + " ] Ready For Use ✅👍");
                isConnected = true;

            } else {

                delayer(" createTables() ==> DBInFo missing Tables");
                createTableByMissing(getMissingTables(getTables()));
                if (isTables()) {
                    isConnected = true;
                    System.out.println("Database [ " + getDatabaseName() + " ] Ready For Use ✅👍");
                }

            }
        }
        System.out.println("--------------------------------------------------------------");
        return isConnected;
    }

    // this will delete the current database
    @Override
    public void deleteDatabase() {
        Scanner input = new Scanner(System.in);
        System.out.println("DATABASE DROPPING");
        System.out.println("-----------------");
        System.out.print("Enter name or leave (blank) to delete de current DB: ");
        String dbName = input.nextLine();
        if (dbName.equalsIgnoreCase("x")) {
            System.out.println("process aborted");
            return;
        }

        if (!dbName.isBlank()) setDatabaseName(dbName);

        if (isDB()) {
            try (Connection connection = DriverManager.getConnection(url, userName, password);) {
                String sqlDropDatabase = "DROP DATABASE " + getDatabaseName() + ";";
                Statement statement = connection.createStatement();
                statement.executeUpdate(sqlDropDatabase);
                System.out.println("DATABASE " + getDatabaseName() + " Deleted ✅");
                setUrl("jdbc:mysql://localhost:3306/");
            } catch (SQLException e) {
                System.out.println("error ❌" + e.getMessage());
            }
        }

    }

    // Rename the Database but be careful ( !!!!! this is a trap read before )
    public void renameDatabase(String newName) {
        deleteDatabase();
        setDatabaseName(newName);
        if (connectDBTechnician()) System.out.println("DataBase rebooted [ new name ==> " + newName + " ] ✅");
        else System.out.println("Failed to Rename DataBase ❌");
    }

    // creating Database for more micro system
    @Override
    public void microDatabase(ArrayList<String> DataBaseNames) {

        try (Connection connection = DriverManager.getConnection(urlNoDBName, userName, password)) {
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement();) {

                for (String nameDB : DataBaseNames) {
                    if (!isDBbyName(nameDB)) {
                        String sqlCreateDataBase = "CREATE DATABASE " + nameDB + ";";
                        statement.executeUpdate(sqlCreateDataBase);
                        System.out.println("DataBase [ " + nameDB + " ] OK ✅");
                        setUrl(urlNoDBName + nameDB);
                        setDatabaseName(nameDB);
                        createTables();
                        System.out.println("************************");
                    } else {
                        System.out.println(" [ " + nameDB + " ] exist ❌");
                    }
                }
                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Error creating Database [ MicroDataBase ] ");
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    // --------------------------------------------------------------------------


    // checking tables in the database
    @Override
    public boolean isTables() {
        setUrl("jdbc:mysql://localhost:3306/" + getDatabaseName());
        boolean isTable = false;
        String sqlCheckTables = """
                SHOW TABLES;
                """;
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlCheckTables);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> dataBaseTabes = new ArrayList<>();
            while (resultSet.next()) {
                String name = resultSet.getString(1);
                dataBaseTabes.add(name);
            }
            isTable = requiredTables(dataBaseTabes);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isTable;
    }

    @Override
    public ArrayList<String> getTables() {

        setUrl("jdbc:mysql://localhost:3306/" + getDatabaseName());
        boolean isTable = false;
        String sqlCheckTables = """
                SHOW TABLES;
                """;
        ArrayList<String> dataBaseTabes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlCheckTables);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString(1);
                dataBaseTabes.add(name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataBaseTabes;
    }

    // create tables
    @Override
    public void createTables() {
        setUrl("jdbc:mysql://localhost:3306/" + getDatabaseName());

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
        if (isDB()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("This will continue by Creating Tables for " + getDatabaseName());
            System.out.print("do you want to continue (Y/N): ");
            String confirmCreateTable = scanner.nextLine();
            if (confirmCreateTable.equalsIgnoreCase("Y")) {
                // perform the sql operation to create all the tabes
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
                        getMissingTables(getTables());
                    } catch (SQLException e) {
                        connection.rollback(); // Roll back if any table creation fails
                        System.err.println("Error creating tables: " + e.getMessage());
                    }

                } catch (SQLException e) {
                    System.err.println("Database connection failed: " + e.getMessage());
                }
                // end of the sql operation
            }
        } else {
            createDatabase();
        }

    }

    // checks if all the required tables exist
    @Override
    public boolean requiredTables(ArrayList<String> presentTables) {

        ArrayList<String> requiredTable = new ArrayList<>();

        String columns = "";
        try (FileReader reader = new FileReader("src/DBOperations/DatabaseResource/table.csv")) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                columns = line;
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("System Error reading (table.csv) ... no connection to tables authenticity");
            e.printStackTrace();
        }

        String[] splitedColumns = columns.split(",");
        for (String column : splitedColumns) {
            requiredTable.add(column.trim());
        }
        requiredTable.replaceAll(String::toLowerCase);
        presentTables.replaceAll(String::toLowerCase);

        return presentTables.containsAll(requiredTable);
    }

    @Override
    public ArrayList<String> getMissingTables(ArrayList<String> presentTables) {
        ArrayList<String> missingTables = new ArrayList<>();
        ArrayList<String> requiredTable = new ArrayList<>();
        String columns = "";
        try (FileReader reader = new FileReader(fileTables)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                columns = line;
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("System Error reading (table.csv) ... no connection to tables authenticity");
            e.printStackTrace();
            return missingTables;
        }
        String[] splitedColumns = columns.split(",");
        for (String column : splitedColumns) {
            requiredTable.add(column.trim());
        }

        presentTables.replaceAll(String::toLowerCase);
        requiredTable.replaceAll(String::toLowerCase);


        missingTables = requiredTable.stream().filter(table -> !presentTables.contains(table)).collect(Collectors.toCollection(ArrayList::new));
        System.out.println("Missing Tables => "+missingTables+" => "+missingTables.size());
        return missingTables;
    }

    @Override
    public void createTableByMissing(ArrayList<String> missingTables) {
        // establish connection to the database
        try (Connection connect = DriverManager.getConnection(url, userName, password)) {
            connect.setAutoCommit(false); // begin transaction

            // read the csv file of tables query creating tables
            try (FileReader reader = new FileReader(fileTablesCreateQuery)) {
                BufferedReader reading = new BufferedReader(reader);
                String line;
                // read line by line
                while ((line = reading.readLine()) != null) {
                    String[] splited = line.split(",");
                    // extract the missing tables
                    for (String table : missingTables) {
                        // compare if they are matching for extracting the query to create this table
                        if (splited[0].equalsIgnoreCase(table)) {
                            StringBuilder query = new StringBuilder();
                            // get from the second element because the first one is the table name , then the query
                            for (int i = 1; i < splited.length; i++) {
                                query.append(splited[i]);
                                if (i < splited.length - 1) {
                                    query.append(",");
                                }
                            }
                            String creatingQueryTable = "CREATE TABLE " + table + " (" + query + ");";
                            try (Statement statement = connect.createStatement()) {
                                statement.executeUpdate(creatingQueryTable);
                                System.out.println("Tables OK ✅");
                                query.delete(0,query.length());

                            } catch (SQLException e) {
                                connect.rollback(); // Roll back if any table creation fails
                                System.err.println("Error creating tables: " + e.getMessage());
                            }
                        }
                    }
                }
                connect.commit(); // Commit transaction
            } catch (IOException e) {
                System.out.println("Error at createTableByMissing(): " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Error at createTableByMissing() : " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------------------

    // delay the process for moving to the next step using a counter time
    public void delayer(String process) {
        int totalSteps = 10;  // Total steps for the progress bar
        for (int i = 0; i <= totalSteps; i++) {
            try {
                Thread.sleep(300);  // Wait for 500 milliseconds before each step

                // Calculate the percentage
                int percentage = (int) ((double) (i) / totalSteps * 100);

                // Clear the current line and print the percentage
                System.out.print("\r[" + percentage + "%]" + " process ==> " + process);

                System.out.flush();  // Ensure the output is immediately written to the console
            } catch (InterruptedException e) {
                System.err.println("Time interrupted");
            }
        }
        System.out.println();  // Print a newline after the progress bar is finished
    }

    // ====================================================================================================


    // ============== AFTER-CONNECTION  DML ====================================================================


    // get all the Data from a table
    @Override
    public void getAllDataFromTable(String tableName) {
        String sqlQueryToRetrieveData = "SELECT * FROM " + tableName + ";";
        try (Connection connection = DriverManager.getConnection(url, userName, password)) {

            // printable is a method dynamic to print any sql table located in SQLTablePrinter

            SQLTablePrinter.printTable(connection, sqlQueryToRetrieveData);
        } catch (SQLException e) {
            System.out.println("Error [ getAllDataFromTable ] " + e.getMessage());
        }
    }


    // select data by column
    @Override
    public void getFromTableSelectByColumn() {
        Scanner input = new Scanner(System.in);
        ArrayList<String> tables = getTables();
        System.out.println("Tables of " + getDatabaseName());
        for (int i = 0; i < new StringBuilder("Tables of ").append(getDatabaseName()).length(); i++) {
            System.out.print("-");
        }
        System.out.println();
        for (int i = 0; i < tables.size(); i++) {
            System.out.println((i + 1) + ". " + tables.get(i));
        }


        System.out.print("Enter the table name: ");
        String tableName = input.nextLine();
        System.out.print("Enter the column name: ");
        String columnName = input.nextLine();
        System.out.print("Enter the value to search: ");
        String valueToSearch = input.nextLine();
        // Construct the SQL query to retrieve data from the specified table and column by value
        String sqlQueryToRetrieveData = "SELECT * FROM " + tableName + " WHERE " + columnName + " = '" + valueToSearch + "';";

        try (Connection connection = DriverManager.getConnection(url, userName, password);) {
            // printable is a method dynamic to print any sql table located in SQLTablePrinter
            SQLTablePrinter.printTable(connection, sqlQueryToRetrieveData);

        } catch (SQLException e) {
            System.out.println("Error [ getFromTableSelectByColumn ] " + e.getMessage());
        }


    }


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

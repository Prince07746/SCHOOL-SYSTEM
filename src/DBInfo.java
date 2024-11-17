import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;


public class DBInfo {
    String url;
    String userName;
    String password;
    String DatabaseName;

    public DBInfo(String userName, String password, String databaseName) {
        this.url = "jdbc:mysql://localhost:3306/";
        this.userName = userName;
        this.password = password;
        DatabaseName = databaseName;
    }



    public void createDataBase(){
        try{

            Connection connection = DriverManager.getConnection(url,userName,password);
            Statement statement = connection.createStatement();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter DataBase Name: ");
            String name  = scanner.nextLine();
            statement.executeQuery("CREATE DATABASE IF NOT EXIST "+name);
            System.out.println("New DataBase Created: "+name);
            setUrl("jdbc:mysql://localhost:3306/"+name);

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void createTables(){
        try {



            Connection connection = DriverManager.getConnection(url,userName,password);


            String sqlTeacherTable = "CREATE TABLE Teacher (\n" +
                    "    id VARCHAR(50) PRIMARY KEY,\n" +
                    "    name VARCHAR(100) NOT NULL,\n" +
                    "    lastName VARCHAR(100) NOT NULL,\n" +
                    "    gender VARCHAR(10) NOT NULL,\n" +
                    "    age INT NOT NULL,\n" +
                    "    salary DOUBLE NOT NULL\n" +
                    ");\n";
            PreparedStatement createTableTeacher = connection.prepareStatement(sqlTeacherTable);


            String sqlStudentTable = "CREATE TABLE Student (\n" +
                    "    id VARCHAR(50) PRIMARY KEY,\n" +
                    "    name VARCHAR(100) NOT NULL,\n" +
                    "    lastName VARCHAR(100) NOT NULL,\n" +
                    "    gender VARCHAR(10) NOT NULL,\n" +
                    "    age INT NOT NULL\n" +
                    ");\n";

            PreparedStatement createStudentCourse = connection.prepareStatement(sqlStudentTable);




            String sqlCourseTable = "CREATE TABLE Courses (\n" +
                    "    id VARCHAR(50) PRIMARY KEY,\n" +
                    "    name VARCHAR(100) NOT NULL,\n" +
                    "    maxPoints DOUBLE NOT NULL"+
                    ");";
            PreparedStatement createTableCourse = connection.prepareStatement(sqlCourseTable);



            String sqlEnrollmentStudentTable = "CREATE TABLE EnrollmentST (\n" +
                    "id VARCHAR(50) PRIMARY KEY,"+
                    "studentId VARCHAR(50),"+
                    "FOREIGN KEY (studentId) "
                    ;

            PreparedStatement createTableEnrollmentStudent = connection.prepareStatement(sqlEnrollmentStudentTable);

            String sqlEnrollmentTeacherTable = "";
            PreparedStatement createTableEnrollmentTeacher = connection.prepareStatement(sqlEnrollmentTeacherTable);




        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
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
        DBInfo dbInfo = (DBInfo) o;
        return Objects.equals(url, dbInfo.url) && Objects.equals(userName, dbInfo.userName) && Objects.equals(password, dbInfo.password) && Objects.equals(DatabaseName, dbInfo.DatabaseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, userName, password, DatabaseName);
    }
}

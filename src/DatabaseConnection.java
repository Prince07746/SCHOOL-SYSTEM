import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;



public class DatabaseConnection{

    private String name;
    private String username;
    private String password;
    private String url;

    public DatabaseConnection(String name,String username,String password){
        this.url = "jdbc:mysql://localhost:3306/"+name;
        this.name = name;
        this.username = username;
        this.password = password;
    }
   public Connection connectionToDatabase(){
        Connection rootToDataBase = null;
        try{
            rootToDataBase = DriverManager.getConnection(url,username,password);
            System.out.println("\nConnected to Database [ "+getName()+" ]\n");
        } catch(SQLException e){
            System.out.println("Error in Connection: "+e.getMessage());
        }
        return rootToDataBase;
    }

   public void getDatabaseTables(){
       try(Connection connectionForReading = connectionToDatabase();){

           Statement statement = connectionForReading.createStatement();
           ResultSet resultAfterReading = statement.executeQuery("SHOW TABLES");
           System.out.println(getName()+" DataBase");
           System.out.println("-------------------");
           while (resultAfterReading.next()){
               String tables = resultAfterReading.getString(1);
               System.out.println("|"+tables);
           }
           System.out.println("-------------------");

       } catch (SQLException e){
           System.out.println("Error when Reading: "+e.getMessage());
       }
   }
   // read data
   public void getDataOfTable(String tableName){
        try(Connection connectToDataBase = connectionToDatabase()){
            Statement statementToReadTable = connectToDataBase.createStatement();
            ResultSet resultSetOfReadingTable = statementToReadTable.executeQuery("SELECT * FROM "+tableName);
            System.out.println(tableName+" Result");
            System.out.println("-----------------------------------");
            tableColumn(tableName);
            while (resultSetOfReadingTable.next()){
                 if(tableName.equals("class")){
                     int classID = resultSetOfReadingTable.getInt(1);
                     String class_name = resultSetOfReadingTable.getString(2);
                     String teacher_name = resultSetOfReadingTable.getString(3);
                     System.out.println(classID+" , "+class_name+" , "+teacher_name);
                 }
                 else if(tableName.equals("student")){
                     int id = resultSetOfReadingTable.getInt(1);
                     String className = resultSetOfReadingTable.getString(2);
                     String teacherName = resultSetOfReadingTable.getString(3);
                     String birthDay = resultSetOfReadingTable.getString(4);
                     System.out.println(id+" , "+className+" , "+teacherName+" , "+birthDay);

                 }
                 else if(tableName.equals("enrollment")){
                     int enrollmentID = resultSetOfReadingTable.getInt(1);
                     int studentID = resultSetOfReadingTable.getInt(2);
                     int classID = resultSetOfReadingTable.getInt(3);
                     double grade = resultSetOfReadingTable.getDouble(4);
                     System.out.println(enrollmentID+" , "+studentID+" , "+classID+" , "+grade);
                 }
            }

        } catch (SQLException e){
            System.out.println("Error Reading data to table: "+e.getMessage());
        }
   }
   // data column from database
   public void tableColumn(String tableName){
        switch (tableName){
            case "class" ->{
                System.out.println("Class ==> Table");
                System.out.println("clas_id , class_name , teacher_name");
                System.out.println("-----------------------------------");
            }
            case "student" ->{
                System.out.println("student ==> table");
                System.out.println("id , first_name , last_name , birthdate");
                System.out.println("---------------------------------------");
            }
            case  "enrollment" ->{
                System.out.println("enrollment ==> table");
                System.out.println("enrollment_id , student_id , class_id , grade");
                System.out.println("-----------------------------------------------");
            }
        }
   }

   // select data by name
   public void getDataOfTable(String tableName,String name){
        try(Connection connectionForRetrieving = connectionToDatabase()){
            Statement statement = connectionForRetrieving.createStatement();

            if(tableName.equals("class")){
                tableColumn(tableName);
                PreparedStatement preparedStatement =
                        connectionForRetrieving.prepareStatement("SELECT * FROM "+tableName+" WHERE class_name = ?");
                preparedStatement.setString(1,name);
                ResultSet result = preparedStatement.executeQuery();
                while(result.next()){
                    int classID = result.getInt(1);
                    String class_name = result.getString(2);
                    String teacher_name = result.getString(3);
                    System.out.println(classID+" , "+class_name+" , "+teacher_name);
                }
                result.close();
                connectionForRetrieving.close();
            }
            else if(tableName.equals("student")){
                tableColumn(tableName);
                PreparedStatement preparedStatement =
                        connectionForRetrieving.prepareStatement("SELECT * FROM "+tableName+" WHERE first_name = ?");
                preparedStatement.setString(1,name);
                ResultSet result = preparedStatement.executeQuery();
                while (result.next()){
                    int id = result.getInt(1);
                    String className = result.getString(2);
                    String teacherName = result.getString(3);
                    String birthDay = result.getString(4);
                    System.out.println(id+" , "+className+" , "+teacherName+" , "+birthDay);
                }

                result.close();
                connectionForRetrieving.close();
            }
            else if(tableName.equals("enrollment")){
                ResultSet result =
                        statement.executeQuery("SELECT student.first_name,student.last_name" +
                                ",enrollment.grade , class.class_name ,class.teacher_name FROM enrollment INNER JOIN " +
                                "student ON " +
                                "enrollment.student_id = " +
                                "student.id INNER JOIN class ON enrollment.class_id = class.class_id");
                System.out.println("enrollment ==> table");
                System.out.println("First_Name , Last_Name , class_Name , Teacher_Name , Grade");
                System.out.println("----------------------------------------------------------");
                while(result.next()){
                    String firstN = result.getString("first_name");
                    String lastN = result.getString("last_name");
                    String classN = result.getString("class_name");
                    String teacherN = result.getString("teacher_name");
                    double grade = result.getDouble("grade");
                    System.out.println(firstN+" , "+lastN+" , "+classN+" , "+teacherN+" , "+grade);
                }
                System.out.println("----------------------------------------------------------");

            result.close();
            connectionForRetrieving.close();
            }

        } catch (SQLException e){
            System.out.println("Error when reading data by name: "+e.getMessage());
        }
   }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
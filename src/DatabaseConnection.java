import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;





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
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            System.out.println("Connected to Database [ "+getName()+" ]");
            rootToDataBase = connection;
        } catch(SQLException e){
            System.out.println("Error in Connection: "+e.getMessage());
        }
        return rootToDataBase;
    }

   public void readData(){

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
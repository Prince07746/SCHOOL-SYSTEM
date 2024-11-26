package SchoolCore.SchoolSystem;


public class School implements SchoolSystem{
    private String name;
    private String password;
    private boolean login;
 //   DBInfo dataBaseInformation = new DBInfo();


    public School(String name,String password){
        this.name = name;
        this.password = password;
        this.login = login();
    }


    @Override
    public boolean isLogin(){
    return  true;
    }



    // setters and getters
    public boolean login() {
        return login;
    }
    public void setLogin(boolean login) {
        this.login = login;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


}

public class Main {

    public static void main(String[] args){
        DBManager db = new DBManager("root","selemani","company");
        System.out.println(db.connectDBTechnician());
    }

}





public class Main {

    public static void main(String[] args){
        DBManager db = new DBManager("root","selemani","company");
        // check integrity
        System.out.println(db.connectDBTechnician());
    }

}





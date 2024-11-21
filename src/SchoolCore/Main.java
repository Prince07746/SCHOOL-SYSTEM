package SchoolCore;

import DBOperations.DBManager;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args){

        DBManager db = new DBManager("root","selemani","","prince");
        // check integrity
        //  System.out.println(db.connectDBTechnician());

        //  db.deleteDatabase();

       ArrayList<String> myDBs = new ArrayList<>(Arrays.asList("school1","school2","school3"));

       db.microDatabase(myDBs);



    }

}





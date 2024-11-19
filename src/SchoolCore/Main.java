package SchoolCore;

import DBOperations.DBManager;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args){
        DBManager db = new DBManager("root","selemani","");
        // check integrity

        ArrayList<String> myDBs = new ArrayList<>(Arrays.asList("school1","school2","school3"));

        db.microDatabase(myDBs);


        DBManager db2 = new DBManager("root","selemani","","school1");
        db2.connectDBTechnician();

        DBManager db3 = new DBManager("root","selemani","","school2");
        db3.connectDBTechnician();

        DBManager db4 = new DBManager("root","selemani","","school3");
        db4.connectDBTechnician();


    }

}





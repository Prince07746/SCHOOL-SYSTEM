package SchoolCore;

import DBOperations.DBManager;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        DBManager db = new DBManager("root","selemani","","school1");
        db.connectDBTechnician();
        db.getAllDataFromTable("student");

        // check integrity ==> this the most entry point of the all system because it can create a database if not
        // yet and tables and fix all the small problem that you might have
        // in upcoming version it will have a lot of functionalities in order to keep the system run

         System.out.println(db.connectDBTechnician());

        // this will delete a database if empty name this will delete the actual database if connected
         db.deleteDatabase();

         ArrayList<String> myDBs = new ArrayList<>(Arrays.asList("school1","school2","school3"));

         // this will create multiple database simultaneously  for upcoming school
         db.microDatabase(myDBs);



    }

}





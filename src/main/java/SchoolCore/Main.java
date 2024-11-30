package SchoolCore;

// spring boot modules
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

import DBOperations.*;

@RestController
@EnableAutoConfiguration
public class Main {
    DBManager db = new DBManager("root","selemani","","school1");

    public  Main() {
        db.connectDBTechnician();
    }
    @RequestMapping("/")
    public String home() {
        return "welcome to this app we have the following tables ==> /getTables , /getMissingTable";
    }
    @RequestMapping("/getTables")
    public String getTable() {
        return db.getTables().toString();
    }
    @RequestMapping("/getMissingTable")
    public String getMissingTable() {
        return db.getMissingTables(db.getTables()).toString();
    }

    public static void main(String[] args) {


        SpringApplication.run(Main.class, args);

    }
}



















//package java.SchoolCore;
//
//import DBOperations.DBManager;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class Main {
//
//    public static void main(String[] args) {
//
//        // don't worry about any setting the connectDBTechnician() will do everything
//
//        DBManager db = new DBManager("root","selemani","","school1");
//        db.connectDBTechnician();
//
//
//   //     db.getFromTableSelectByColumn();
//
//
//
////        // this will read the data from the table student
////        db.getAllDataFromTable("student");
////
////        // check integrity ==> this the most entry point of the all system because it can create a database if not
////        // yet and tables and fix all the small problem that you might have
////        // in upcoming version it will have a lot of functionalities in order to keep the system run
////
////         System.out.println(db.connectDBTechnician());
////
////        // this will delete a database if empty name this will delete the actual database if connected
////         db.deleteDatabase();
////
////         ArrayList<String> myDBs = new ArrayList<>(Arrays.asList("school1","school2","school3"));
////
////         // this will create multiple database simultaneously  for upcoming school
////         db.microDatabase(myDBs);
//
//
//
//    }
//
//}
//
//
//
//

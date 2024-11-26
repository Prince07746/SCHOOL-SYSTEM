package SchoolCore;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import DBOperations.*;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        DBManager db = new DBManager("root","selemani","","school1");
        db.connectDBTechnician();
        Label label = new Label(db.getTables().toString());
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 700, 600);

        primaryStage.setTitle("JavaFX Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
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

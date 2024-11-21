package DBOperations;
import java.util.ArrayList;


public interface DbOperationInterface {

    boolean isDB();

    void isDataBaseBlankFixing();

    boolean isDBbyName(String dbName);

    void microDatabase(ArrayList<String> databaseNames);

    boolean isTables();

    void createTables();

    void createDatabase();

    boolean connectDBTechnician();

    void deleteDatabase();

    boolean requiredTables(ArrayList<String> presentTables);

    void renameDatabase(String newName);
}

package DBOperations;
import java.util.ArrayList;


public interface DbOperationInterface {

    boolean isDB();

    void isDataBaseBlankFixing();

    boolean isDBbyName(String dbName);

    void microDatabase(ArrayList<String> databaseNames);

    void createTables();

    void createDatabase();

    boolean connectDBTechnician();

    void deleteDatabase();

    void renameDatabase(String newName);

    boolean requiredTables(ArrayList<String> presentTables);

    boolean isTables();

    // DML ========= DML QUERY =================================

    public void getAllDataFromTable(String tableName);


}

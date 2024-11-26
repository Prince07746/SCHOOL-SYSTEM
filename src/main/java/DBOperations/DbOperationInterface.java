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

    void createTableByMissing(ArrayList<String> missingTables);

    ArrayList<String> getMissingTables(ArrayList<String> presentTables);
    // DML ========= DML QUERY =================================

    public void getAllDataFromTable(String tableName);

    public ArrayList<String> getTables();

    public void  getFromTableSelectByColumn();

}

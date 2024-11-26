package DBOperations.SqlTableUI;

import java.sql.*;

public class SQLTablePrinter {
    public static void printTable(Connection conn, String query)  {

        try(PreparedStatement preparedStatement = conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = preparedStatement.executeQuery();

        ){
            // get the metadata about the table like column , column names
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            // get the total number of columns
            int countColumn = resultSetMetaData.getColumnCount();

            // Calculate Column Widths
            int[] columnWidths = new int[countColumn];
            for(int i = 1; i <= countColumn; i++){
                columnWidths[i - 1] = resultSetMetaData.getColumnName(i).length();
            }
            // Determine Maximum Width of Each Column
            while (resultSet.next()){
                for(int i = 1; i <= countColumn; i++){
                   String data = resultSet.getString(i);
                   if(data != null){
                       columnWidths[i - 1] = Math.max(columnWidths[i - 1],data.length());
                   }
                }
            }
            // Resets the ResultSet cursor to the beginning so rows can be iterated again.
            resultSet.beforeFirst();

            // Print Colum data type
            int typesLength = 0;
            for (int i = 1; i <= countColumn; i++) {

                StringBuilder types = new StringBuilder();
                types.append(
                        resultSetMetaData.getColumnName(i))
                        .append(" ")
                        .append(resultSetMetaData.getColumnTypeName(i))
                        .append("[")
                        .append(resultSetMetaData.getColumnDisplaySize(i))
                        .append("] |");
                System.out.print(types);
                typesLength += types.length();
            }
            System.out.println();
            for(int i = 0; i <= typesLength; i++){
                System.out.print("-");
            }
            System.out.println();
            // ------------------------------------------

            // Print Column Headers
            for (int i = 1; i <= countColumn; i++) {
                System.out.printf("%-" + columnWidths[i - 1] + "s | ",
                        resultSetMetaData.getColumnName(i));
            }
            System.out.println();
            // ------------------------------------------


            // Print Separator Line
            for (int width : columnWidths) {
                System.out.print("-".repeat(width) + "-+-");
            }
            System.out.println();
            // -------------------------------------------


            // Print Rows
            while (resultSet.next()) {
                for (int i = 1; i <= countColumn; i++) {
                    String data = resultSet.getString(i) == null ? "NULL" : resultSet.getString(i);
                    System.out.printf("%-" + columnWidths[i - 1] + "s | ", data);
                }
                System.out.println();
            }
            // ---------------------------------------------
        } catch (SQLException e){
            System.out.println("Error [ Printable() in SQLTableUI ]  "+e.getMessage());
        }

    }
}

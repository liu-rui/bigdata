import java.sql.*;

public class App {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("com.cloudera.impala.jdbc41.Driver");

        Connection connection = DriverManager.getConnection("jdbc:impala://host17218115135:21050/default");



        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select count(0) from hbasestringids");

        while(resultSet.next()){
            System.out.println(resultSet.getString(1));
        }
    }
}

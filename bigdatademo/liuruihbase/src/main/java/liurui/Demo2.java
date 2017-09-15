package liurui;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/***
 * 自增列
 * 1. 不存在时自动创建
 * 2. 类型为long
 */
public class Demo2 {

    public static final TableName TABLE_NAME = TableName.valueOf("tbl1");
    public static final byte[] COLUMN_FAMILY = "info".getBytes();
    public static final byte[] COLUMN = "count".getBytes();

    public static void main(String[] args) throws IOException {
        try (Connection connection = ConnectionFactory.createConnection()) {
            try (Admin admin = connection.getAdmin()) {
                if (admin.tableExists(TABLE_NAME)) {
                    admin.disableTable(TABLE_NAME);
                    admin.deleteTable(TABLE_NAME);
                }
                HTableDescriptor tableDescriptor = new HTableDescriptor(TABLE_NAME);
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(COLUMN_FAMILY);
                tableDescriptor.addFamily(hColumnDescriptor);
                admin.createTable(tableDescriptor);
            }

            try (Table table = connection.getTable(TABLE_NAME)) {
                for (int i = 0; i < 100; i++)
                    table.incrementColumnValue("xss".getBytes(), COLUMN_FAMILY, COLUMN, 1);
                Result result = table.get(new Get("xss".getBytes()));
                System.out.println(Bytes.toLong(result.getValue(COLUMN_FAMILY, COLUMN)));
            }
        }
    }
}

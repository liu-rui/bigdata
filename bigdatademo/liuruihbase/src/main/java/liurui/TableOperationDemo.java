package liurui;

import com.demo.AddressProtos;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.exceptions.DeserializationException;

import java.io.Closeable;
import java.io.IOException;

/***
 * hbase表操作
 * 1. 创建表
 * 2. 生成数据．格式为protobuffer
 * 3.查询数据
 */
public class TableOperationDemo {
    public static class First implements Closeable {
        private static final TableName TABLE_NAME = TableName.valueOf("first");
        private static final byte[] COLUMN_FAMILY = "info".getBytes();
        private static final byte[] COLUMN = "address".getBytes();
        Admin admin;
        Table table;

        public First() throws IOException, DeserializationException {
            begin();
        }

        public void begin() throws IOException, DeserializationException {
            Configuration configuration = HBaseConfiguration.create();
            //configuration.set("zookeeper.znode.parent", "/hbase-unsecure");
            //configuration.set("hbase.zookeeper.quorum", "host17218115111,host17218115112,host17218115113");

            Connection connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
            table = connection.getTable(TABLE_NAME);
        }

        public void createTable() throws IOException {
            if (admin.tableExists(TABLE_NAME)) {
                admin.disableTable(TABLE_NAME);
                admin.deleteTable(TABLE_NAME);
            }
            HTableDescriptor tableDescriptor = new HTableDescriptor(TABLE_NAME);
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(COLUMN_FAMILY);

            tableDescriptor.addFamily(hColumnDescriptor);
            admin.createTable(tableDescriptor);
        }

        public void genericData() throws IOException {
            AddressProtos.Address.Builder builder = AddressProtos.Address.newBuilder();

            for (int i = 0; i < 100; i++) {
                builder.setCity(Integer.toString(i));
                builder.setStreet(Integer.toString(i));
                Put put = new Put(String.format("%d_%d", i % 2, i).getBytes());
                put.addColumn(COLUMN_FAMILY, COLUMN, builder.build().toByteArray());
                table.put(put);
            }
        }

        /***
         * 查询row key以 1开头的行
         * @throws IOException
         */
        public void showData() throws IOException {
            Scan scan = new Scan();
            scan.setRowPrefixFilter("1_".getBytes());
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                byte[] value = result.getValue(COLUMN_FAMILY, COLUMN);
                AddressProtos.Address address = AddressProtos.Address.parseFrom(value);
                System.out.println(String.format("%s:%s", new String(result.getRow()), address));
            }
        }


        @Override
        public void close() throws IOException {
            if (admin != null) admin.close();
            if (table != null) table.close();
        }
    }

    public static void main(String[] args) throws IOException, DeserializationException {
        try (First first = new First()) {
            first.createTable();
            first.genericData();
            first.showData();
        }
    }
}

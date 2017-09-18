import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/***
 * 注意必须在hiveserver2所在的服务器上运行
 *
 * 1. 将jar上传到hdfs
 * hdfs dfs -put b.jar /
 * 2.创建函数,名为tuo
 * CREATE TEMPORARY FUNCTION tuo AS 'hive.Tuo' USING JAR "hdfs:///b.jar";
 * 3.使用函数
 * select tuo(name) from tbl2;
 */
public final class Tuo extends UDF {
    public Text evaluate(final Text s) {
        if (s == null) {
            return null;
        }

        String ret = s.toString();

        ret = String.format("%c***%c", ret.charAt(0), ret.charAt(ret.length() - 1));
        return new Text(ret);
    }
}

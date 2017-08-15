import org.apache.hadoop.conf.Configuration;

/***
 * 操作配置文件
 */
public class ConfigDemo {
    public static void main(String[] args) {
        Configuration conf = new Configuration();

        conf.addResource("1.xml");
        System.out.println(conf.get("dfs.name.dir"));
        System.out.println(conf.get("dfs.data.dir"));

        conf.addResource("2.xml");
        System.out.println(conf.get("dfs.name.dir"));
        //dfs.data.dir 已经被覆盖
        System.out.println(conf.get("dfs.data.dir"));
    }
}

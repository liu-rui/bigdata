package com.github.liurui.wordcount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import java.io.IOException;

/**
 * Hello world!
 * storm jar storm-1.0-SNAPSHOT.jar com.github.liurui.wordcount.WorldCount
 */
public class WorldCount {
    public static void main(String[] args) throws InterruptedException, IOException, InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("mySpout", new MyFirstSpout(), 1);
        builder.setBolt("splitBolt", new SplitBlot(), 1).shuffleGrouping("mySpout");
        builder.setBolt("CountBolt", new CountBolt(), 1).fieldsGrouping("splitBolt", new Fields("word"));
        builder.setBolt("reportBolt", new ReportBolt()).globalGrouping("CountBolt");
        Config config = new Config();

        if (args.length == 0) {
            LocalCluster cluster = new LocalCluster();

            cluster.submitTopology("first", config, builder.createTopology());

            Thread.sleep(1000 * 30);
            cluster.killTopology("first");
            cluster.shutdown();
        } else {
            StormSubmitter.submitTopology(args[0], config, builder.createTopology());
        }
    }
}

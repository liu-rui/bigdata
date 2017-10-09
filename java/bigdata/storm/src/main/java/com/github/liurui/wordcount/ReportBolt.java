package com.github.liurui.wordcount;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

public class ReportBolt extends BaseRichBolt {
    Map<String, Integer> counts = new HashMap<>();
    private OutputCollector collector;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String word = input.getString(0);
        System.err.printf("ReportBolt begin msgId:%s value:%s%n", input.getMessageId(), word);
        counts.put(word, input.getInteger(1));
        System.err.printf("ReportBolt end msgId:%s value:%s%n", input.getMessageId(), input.getString(0));
        collector.ack(input);
    }

    @Override
    public void cleanup() {
        System.err.println("------统计结果------");
        counts.forEach((k, v) -> System.err.printf("key:%s count:%d %n", k, v));
        System.err.println("---------------");
    }
}

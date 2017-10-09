package com.github.liurui.wordcount;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class CountBolt extends BaseRichBolt {
    Map<String, Integer> counts = new HashMap<>();
    private OutputCollector collector;


    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word", "count"));
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        System.err.printf("CountBolt begin msgId:%s value:%s%n", input.getMessageId(), input.getString(0));
        String word = input.getString(0);
        int ret = Utils.get(counts, word, 0) + 1;

        counts.put(word, ret);
        collector.emit(input , new Values(word, ret));
        System.err.printf("CountBolt end msgId:%s value:%s%n", input.getMessageId(), input.getString(0));
        collector.ack(input);
    }
}

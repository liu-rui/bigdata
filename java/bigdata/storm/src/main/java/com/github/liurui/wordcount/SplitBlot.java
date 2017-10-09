package com.github.liurui.wordcount;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class SplitBlot extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {

        System.err.printf("splitBlot begin msgId:%s value:%s%n", input.getMessageId(), input.getString(0));
        String[] strings = input.getString(0).split(" ");
        for (String string : strings) {
            collector.emit(input, new Values(string));
        }
        System.err.printf("splitBlot end%n");
        this.collector.ack(input);
    }
}

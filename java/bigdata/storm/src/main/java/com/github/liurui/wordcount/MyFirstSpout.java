package com.github.liurui.wordcount;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MyFirstSpout extends BaseRichSpout {
    public static final String[] SENTENCES = new String[]{"hello world", "hello hadoop"};
    private SpoutOutputCollector collector;
    private Random rnd;
    private int count = 0;
    private ConcurrentHashMap<UUID, Values> values;


    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        rnd = new Random(System.currentTimeMillis());
        values = new ConcurrentHashMap<>();
    }

    @Override
    public void nextTuple() {
        Utils.sleep(100);
        String sentence = SENTENCES[rnd.nextInt(SENTENCES.length)];
        UUID newUUID = UUID.randomUUID();

        Values data = new Values(sentence);
        collector.emit(data, newUUID);
        values.put(newUUID, data);
        System.err.printf("spout 发射：%s msgID:%s %n", sentence, newUUID.toString());
        count++;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("sentence"));
    }

    @Override
    public void ack(Object msgId) {
        System.err.printf("spout ack msgid:%s%n", msgId);
        values.remove(msgId);
    }

    @Override
    public void fail(Object msgId) {
        System.err.printf("spout fail msgid:%s%n", msgId);
        collector.emit(values.get(msgId), msgId);
    }
}
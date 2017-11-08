package com.github.liurui;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.net.InetSocketAddress;

/**
 * Hello world!
 *
 */

@SpringBootApplication
public class App 
{
    @Value("${elasticsearch.user}")
    String user;

    @Value("${elasticsearch.clusterNodes}")
    String clusterNodes;

    @Bean
    public Client client(){
        String[] nodes = clusterNodes.split(",");
        PreBuiltXPackTransportClient client = new PreBuiltXPackTransportClient(Settings.builder()
                .put("client.transport.sniff", true)
                .put("xpack.security.user", user)
                .build());

        for (String node : nodes){
            String[] items = node.split(":");

            client.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(items[0] , Integer.parseInt(items[1]))));
        }
        return client;
    }

    public static void main( String[] args )
    {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
        ElasticsearchTemplate template = context.getBean(ElasticsearchTemplate.class);

        System.out.println(template.indexExists("index"));
    }
}

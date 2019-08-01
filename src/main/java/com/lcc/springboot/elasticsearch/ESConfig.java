package com.lcc.springboot.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author 李歘歘
 * ES配置文件
 */
@Configuration
public class ESConfig {


    @Bean
    public TransportClient client() throws UnknownHostException {
        InetSocketTransportAddress node = new InetSocketTransportAddress(
                //此处为tcp端口
                InetAddress.getByName("localhost"),9300

        );

        //elasticsearch配置
        Settings settings = Settings.builder()
                .put("cluster.name","lcc")
                .build();


        TransportClient client = new PreBuiltTransportClient(settings);
        //将地址传递给client，使其在这个地址发起请求
        //在集群环境下，可以创建多个node并传递进来
        client.addTransportAddress(node);
        return client;
    }

}

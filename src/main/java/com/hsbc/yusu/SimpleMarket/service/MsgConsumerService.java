package com.hsbc.yusu.SimpleMarket.service;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hsbc.yusu.SimpleMarket.config.TopicConfigurations;
import com.hsbc.yusu.SimpleMarket.util.MsgGenerator;

import lombok.extern.slf4j.Slf4j;

/**
 * @author esuxyux
 *
 */
@Slf4j
@EnableAsync
@EnableScheduling
@Service
public class MsgConsumerService {

	@Autowired
	private MsgGenerator msgGenerator;

	@Autowired
	private TopicConfigurations configurtion;

	@Value("${kafka.consumer.bootstrap-servers}")
	private String bootstrapServer;

	@Async
	@Scheduled(cron = "0 0/5 * * * ?")
	public void consumer1() {
		List<TopicConfigurations.Topic> topics = configurtion.getTopics();
		List subTopic = topics.stream().map(TopicConfigurations.Topic::getName).collect(Collectors.toList());
		log.info("consumer1 start to consume data");
		KafkaConsumer consumer = getKafkaConsumer("consumer1");
		log.info(Thread.currentThread().getName() + "--tradeTopic--consumer1 create successful..");
		consumer.subscribe(subTopic);

		ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

		for (ConsumerRecord<String, String> record : records) {
			log.info(record.topic() + "new message offset = {}, key = {}, value = {}", record.offset(), record.key(), record.value());
			log.info("consumer_1 consumer data, the message is {}", record.value());
			msgGenerator.saveConsumerData(1, record.value());
		}

		if (records.count() > 0) {
			consumer.commitSync();
		}
	}

	@Async
	@Scheduled(cron = "0 0/1 * * * ?")
	public void consumer2() {
		List<TopicConfigurations.Topic> topics = configurtion.getTopics();
		List subTopic = topics.stream().map(TopicConfigurations.Topic::getName).collect(Collectors.toList());
		log.info("consumer2 start to consume data");
		KafkaConsumer consumer = getKafkaConsumer("consumer2");
		log.info(Thread.currentThread().getName() + "--tradeTopic--consumer2 create successful..");
		consumer.subscribe(subTopic);

		ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

		for (ConsumerRecord<String, String> record : records) {
			log.info(record.topic() + "new message offset = {}, key = {}, value = {}", record.offset(), record.key(), record.value());
			log.info("consumer_2 consumer data, the message is {}", record.value());
			msgGenerator.saveConsumerData(2, record.value());
		}

		if (records.count() > 0) {
			consumer.commitSync();
		}
	}

	public KafkaConsumer getKafkaConsumer(String group) {
		Properties propstask = new Properties();
		propstask.put("bootstrap.servers", bootstrapServer);
		// set the group id for consume
		propstask.put("group.id", group);
		propstask.put("enable.auto.commit", "false");
		// set the message poll count
		propstask.put("max.poll.records", 10000);
		propstask.put("auto.commit.interval.ms", "1000");
		propstask.put("session.timeout.ms", "30000");
		// reset offset
		propstask.put("auto.offset.reset", "latest");
		propstask.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		propstask.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		return new KafkaConsumer<String, String>(propstask);
	}

}

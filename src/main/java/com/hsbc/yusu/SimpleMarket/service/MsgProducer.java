package com.hsbc.yusu.SimpleMarket.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hsbc.yusu.SimpleMarket.config.TopicConfigurations;
import com.hsbc.yusu.SimpleMarket.model.TradeRecord;
import com.hsbc.yusu.SimpleMarket.util.JSONUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author esuxyux
 *
 */
@Slf4j
@Service
public class MsgProducer {
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private TopicConfigurations configurtion;

	public void sendMessage(List<TradeRecord> tradeRecord) {
		List<TopicConfigurations.Topic> topics = configurtion.getTopics();
		
		List<TradeRecord> enrichRecordList = tradeRecord.stream().map(t -> enrichMessage(t)).collect(Collectors.toList());
		
		String message = null;
		try {
			message = JSONUtil.objectToJson(enrichRecordList);
		} catch (JsonProcessingException e) {
			log.error("convert trade record failed");
		}
		for (TopicConfigurations.Topic t : topics) {
			ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(t.getName(), message);
			future.addCallback(result -> log.info("Send trade message to topic:{} partition:{} successful", result.getRecordMetadata().topic(), result.getRecordMetadata().partition()), ex -> log.error("Send trade message failed, Reason:{}", ex.getMessage()));
		}
	}

	private TradeRecord enrichMessage(TradeRecord record) {
		BigDecimal quantity = new BigDecimal(record.getQuantity());
		BigDecimal price = new BigDecimal(record.getPrice());
		BigDecimal amount = quantity.multiply(price).setScale(2, RoundingMode.HALF_UP);
		record.setAmount(amount.toPlainString());

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		record.setReceivedTimestamp(dateFormat.format(cal.getTime()));

		return record;
	}

}

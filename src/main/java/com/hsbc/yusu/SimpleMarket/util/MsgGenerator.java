package com.hsbc.yusu.SimpleMarket.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hsbc.yusu.SimpleMarket.model.TradeRecord;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author esuxyux
 *
 */
@Slf4j
@Getter
@Setter
@EnableScheduling
@Component
public class MsgGenerator {

	@Value("${input.message.location}")
	private String inputMessageLocation;

	@Value("${output.message1.location}")
	private String consumer1OutputLocation;

	@Value("${output.message2.location}")
	private String consumer2OutputLocation;

	public File getInputFolder() {
		File monitorFolder = new File(inputMessageLocation);
		if (!monitorFolder.isDirectory()) {
			monitorFolder.mkdirs();
		}
		return monitorFolder;
	}

	/**
	 * schedule generate trade messages for producer
	 */
	@Scheduled(initialDelay = 2000, fixedDelay = 20000)
	public void generateMessages() {
		TradeRecord record = new TradeRecord();
		record.setTradeReference("T" + (int) ((Math.random() * 9 + 1) * 10000));
		record.setAccountNumber("1300218277");
		record.setStockCode("KO");
		// generate random quantity, 100-600
		DecimalFormat quantityDf = new DecimalFormat("0.000000");
		record.setQuantity(quantityDf.format(100 + Math.random() * 500));
		record.setCurrency("USD");
		DecimalFormat priceDf = new DecimalFormat("0.00");
		record.setPrice(priceDf.format(100 + Math.random() * 500));
		record.setBroker("B00123");
		try {
			String message = JSONUtil.objectToJson(Collections.singletonList(record));
			log.info("generate one trade message, message content is \n {}", message);
			String filePath = getInputFolder().getCanonicalPath() + File.separator + "trade_" + System.currentTimeMillis() + ".json";
			FileUtils.writeStringToFile(new File(filePath), message, Charset.defaultCharset(), false);
		} catch (IOException e) {
			log.error("generate trade message failed, {}", e);
		}
	}

	public void saveConsumerData(int type, String message) {
		File outputFolder = null;
		String filePath = null;
		if (type == 1) {
			outputFolder = new File(consumer1OutputLocation);
		} else if (type == 2) {
			outputFolder = new File(consumer2OutputLocation);
		}
		if (!outputFolder.isDirectory()) {
			outputFolder.mkdirs();
		}
		try {
			filePath = outputFolder.getCanonicalPath() + File.separator + "consumer_" + System.currentTimeMillis() + ".json";
			FileUtils.writeStringToFile(new File(filePath), message, Charset.defaultCharset(), false);
		} catch (IOException e) {
			log.error("get output file folder failed {}", e);
		}

	}

}

package com.hsbc.yusu.SimpleMarket.monitor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hsbc.yusu.SimpleMarket.model.TradeRecord;
import com.hsbc.yusu.SimpleMarket.service.MsgProducer;
import com.hsbc.yusu.SimpleMarket.util.JSONUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author esuxyux
 *
 */
@Slf4j
public class FileListener extends FileAlterationListenerAdaptor {

	private MsgProducer msgProducer;

	public FileListener(MsgProducer msgProducer) {
		this.msgProducer = msgProducer;
	}

	// Create new file
	@Override
	public void onFileCreate(File file) {
		List<TradeRecord> tradeRecords = null;
		String tradeData = null;
		try {
			tradeData = FileUtils.readFileToString(file, Charset.defaultCharset());
			tradeRecords = JSONUtil.jsonToObject(tradeData, new TypeReference<List<TradeRecord>>() {
			});
		} catch (IOException e) {
			log.error("Read trade data failed", e);
		}
		log.info("[Monitor]: find new trade json data:\n {}", tradeData);

		// send new message to mock mq
		msgProducer.sendMessage(tradeRecords);
	}

}

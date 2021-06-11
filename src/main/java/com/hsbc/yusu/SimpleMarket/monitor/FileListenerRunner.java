package com.hsbc.yusu.SimpleMarket.monitor;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author esuxyux
 *
 */

@Slf4j
@Component
public class FileListenerRunner implements CommandLineRunner {

	@Autowired
	private FileListenerFactory fileListenerFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
	 */
	@Override
	public void run(String... args) throws Exception {
		FileAlterationMonitor fileAlterationMonitor = fileListenerFactory.getMonitor();
		try {
			log.info("Start trade data monitor thread ------->");
			// start file monitor thread
			fileAlterationMonitor.start();
		} catch (Exception e) {
			log.error("trade data monitor thread failed ", e);
		}
	}


}

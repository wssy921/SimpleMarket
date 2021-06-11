package com.hsbc.yusu.SimpleMarket.monitor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hsbc.yusu.SimpleMarket.service.MsgProducer;
import com.hsbc.yusu.SimpleMarket.util.MsgGenerator;

/**
 * @author esuxyux
 *
 */
@Component
public class FileListenerFactory {

	@Autowired
	private MsgGenerator generator;

	// Set the monitor time schedule
	private final long interval = TimeUnit.SECONDS.toMillis(4);

	@Autowired
	private MsgProducer msgProducer;

	public FileAlterationMonitor getMonitor() throws IOException {
		// Create Filter
		IOFileFilter directories = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE);
		IOFileFilter files = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(".json"));
		IOFileFilter filter = FileFilterUtils.or(directories, files);

		FileAlterationObserver observer = new FileAlterationObserver(generator.getInputFolder(), filter);

		observer.addListener(new FileListener(msgProducer));

		return new FileAlterationMonitor(interval, observer);
	}

}

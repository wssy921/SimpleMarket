package com.hsbc.yusu.SimpleMarket.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * @author esuxyux
 *
 */
@Configuration
public class TopicAdministrator {
	private final TopicConfigurations configurations;
	private final GenericWebApplicationContext context;

	// use construct get the spring context
	public TopicAdministrator(TopicConfigurations configurations, GenericWebApplicationContext genericContext) {
		this.configurations = configurations;
		this.context = genericContext;
	}

	@PostConstruct
	public void init() {
		initializeBeans(configurations.getTopics());
	}

	private void initializeBeans(List<TopicConfigurations.Topic> topics) {
		topics.forEach(t -> context.registerBean(t.name, NewTopic.class, t::toNewTopic));
	}
}

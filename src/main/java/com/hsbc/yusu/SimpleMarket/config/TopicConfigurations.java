package com.hsbc.yusu.SimpleMarket.config;

import java.util.List;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * @author esuxyux
 *
 */
@Configuration
@ConfigurationProperties(prefix = "kafka")
@Setter
@Getter
public class TopicConfigurations {
	private List<Topic> topics;

	@Setter
	@Getter
	public static class Topic {
		String name;
		Integer numPartitions = 3;
		Short replicationFactor = 1;

		NewTopic toNewTopic() {
			return new NewTopic(this.name, this.numPartitions, this.replicationFactor);
		}

	}
}

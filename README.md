##Simple Market Trade Mock Server

###Features:
1. Implement kafka producer and monitor the specified folder, if found new json file generated in this folder, it would send message to kafka topic
2. Kafka Topic is automatically created according to configuration, support multiple kafka topics.
3. Using java8 stream API and lambda to calculate the Amount
4. Create kafka consumer timed consume message, consume1 consume message from kafka topic every 5 minutes, and consumer2 consume message from kafka topic every 1 minute.
5. consume use the async and schedule read message from kafka, and save trade message to specified folder.

###Build a jar and run it
```shell
	mvn clean package
```

###Run and Test
1. Download delivery jar file from delivery folder
1. modify config/application.yaml file, moidfy the kafka server address, and input, output folders
2. Run java
```shell
    java -jar SimpleMarket-0.0.1-SNAPSHOT.jar
```

###Test Result:
1. Producer produce messages
```shell
2021-06-11 14:27:38,087 INFO  scheduling-1 com.hsbc.yusu.SimpleMarket.util.MsgGenerator:87 - generate one trade message, message content is 
 [{"tradeReference":"T61489","accountNumber":"1300218277","stockCode":"KO","quantity":"204.299014","currency":"USD","price":"447.60","broker":"B00123"}]
2021-06-11 14:27:40,106 INFO  Thread-3 com.hsbc.yusu.SimpleMarket.monitor.FileListener:52 - [Monitor]: find new trade json data:
 [{"tradeReference":"T61489","accountNumber":"1300218277","stockCode":"KO","quantity":"204.299014","currency":"USD","price":"447.60","broker":"B00123"}]
2021-06-11 14:27:40,174 INFO  kafka-producer-network-thread | producer-1 com.hsbc.yusu.SimpleMarket.service.MsgProducer:59 - Send trade message to topic:tradeTopic partition:1 successful
```
2. Consumer1 consume message every 5 minutes
Consume timeslot
```shell
2021-06-11 14:30:00,004 INFO  task-4 com.hsbc.yusu.SimpleMarket.service.MsgConsumerService:57 - consumer1 start to consume data
2021-06-11 14:35:00,001 INFO  task-9 com.hsbc.yusu.SimpleMarket.service.MsgConsumerService:57 - consumer1 start to consume data
```
Conums message content example:
```shell
2021-06-11 14:30:00,258 INFO  task-4 com.hsbc.yusu.SimpleMarket.service.MsgConsumerService:66 - consumer_1 consumer data, the message is [{"tradeReference":"T65651","accountNumber":"1300218277","stockCode":"KO","quantity":"530.520186","currency":"USD","price":"547.28","broker":"B00123","amount":"290343.09","receivedTimestamp":"2021-06-11T14:30:00.252"}]
...
2021-06-11 14:40:00,049 INFO  task-15 com.hsbc.yusu.SimpleMarket.service.MsgConsumerService:66 - consumer_1 consumer data, the message is [{"tradeReference":"T91852","accountNumber":"1300218277","stockCode":"KO","quantity":"168.051283","currency":"USD","price":"220.29","broker":"B00123","amount":"37020.02","receivedTimestamp":"2021-06-11T14:31:20.352"}]
```
3. Consumer2 consume message every 1 minute
Consume timeslot
```shell
2021-06-11 14:28:00,012 INFO  task-1 com.hsbc.yusu.SimpleMarket.service.MsgConsumerService:80 - consumer2 start to consume data
2021-06-11 14:29:00,001 INFO  task-2 com.hsbc.yusu.SimpleMarket.service.MsgConsumerService:80 - consumer2 start to consume data
2021-06-11 14:30:00,002 INFO  task-3 com.hsbc.yusu.SimpleMarket.service.MsgConsumerService:80 - consumer2 start to consume data
2021-06-11 14:31:00,001 INFO  task-5 com.hsbc.yusu.SimpleMarket.service.MsgConsumerService:80 - consumer2 start to consume data
2021-06-11 14:32:00,001 INFO  task-6 com.hsbc.yusu.SimpleMarket.service.MsgConsumerService:80 - consumer2 start to consume data
2021-06-11 14:33:00,002 INFO  task-7 com.hsbc.yusu.SimpleMarket.service.MsgConsumerService:80 - consumer2 start to consume data
2021-06-11 14:34:00,002 INFO  task-8 com.hsbc.yusu.SimpleMarket.service.MsgConsumerService:80 - consumer2 start to consume data
2021-06-11 14:35:00,002 INFO  task-10 com.hsbc.yusu.SimpleMarket.service.MsgConsumerService:80 - consumer2 start to consume data
2021-06-11 14:36:00,000 INFO  task-11 com.hsbc.yusu.SimpleMarket.service.MsgConsumerService:80 - consumer2 start to consume data
```
Conums message content example:
```shell
2021-06-11 14:34:00,038 INFO  task-8 com.hsbc.yusu.SimpleMarket.service.MsgConsumerService:89 - consumer_2 consumer data, the message is [{"tradeReference":"T12336","accountNumber":"1300218277","stockCode":"KO","quantity":"123.520222","currency":"USD","price":"537.53","broker":"B00123","amount":"66395.82","receivedTimestamp":"2021-06-11T14:28:20.176"}]
...
2021-06-11 14:34:00,045 INFO  task-8 com.hsbc.yusu.SimpleMarket.service.MsgConsumerService:89 - consumer_2 consumer data, the message is [{"tradeReference":"T17152","accountNumber":"1300218277","stockCode":"KO","quantity":"253.613232","currency":"USD","price":"327.37","broker":"B00123","amount":"83025.36","receivedTimestamp":"2021-06-11T14:29:20.216"}]
```


https://mp.weixin.qq.com/s/jUylk813LYbKw0sLiIttTQ

https://mp.weixin.qq.com/s/n7JGuYLE_J6uZrcd0tnCeg
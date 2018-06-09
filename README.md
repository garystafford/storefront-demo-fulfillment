# Kafka Traveler Microservices Demo: Fulfillment

## Fulfillment Service

Spring Boot/Kafka/Mongo Microservice, one of a set of microservices for this project. Services use Spring Kafka 2.1.6 to maintain eventually consistent data between their different `Customer` domain objects.

Originally code based on the post, [Spring Kafka - JSON Serializer Deserializer Example](https://www.codenotfound.com/spring-kafka-json-serializer-deserializer-example.html), from the [CodeNotFound.com](https://www.codenotfound.com/) Blog. Original business domain idea based on the post, [Distributed Sagas for Microservices](https://dzone.com/articles/distributed-sagas-for-microservices), on [DZone](https://dzone.com/).

## Development

For [Kakfa](https://kafka.apache.org/), I use my [garystafford/kafka-docker](https://github.com/garystafford/kafka-docker) project, a clone of the [wurstmeister/kafka-docker](https://github.com/wurstmeister/kafka-docker) project. The `garystafford/kafka-docker` [local docker-compose file](https://github.com/garystafford/kafka-docker/blob/master/docker-compose-local.yml) builds a Kafka, Kafka Manager, ZooKeeper, and MongoDB.

## Commands

I develop and debug directly from JetBrains IntelliJ. The default Spring profile will start the three services on different ports.

```bash
./gradlew clean build bootRun
```

## Creating Sample Data

Create sample data for each service. Requires Kafka is running.

```bash
# accounts - create sample customer accounts
http http://localhost:8085/customers/sample

# orders - add sample orders to each customer
http http://localhost:8090/customers/sample/orders

# orders - send approved orders to fulfillment service
http http://localhost:8090/customers/fulfill

# fulfillment - change fulfillment requests from approved to processing
http http://localhost:8095/fulfillment/sample/process

# fulfillment - change fulfillment requests from processing to shipping
http http://localhost:8095/fulfillment/sample/ship

# fulfillment - change fulfillment requests from processing to in transit
http http://localhost:8095/fulfillment/sample/in-transit

# fulfillment - change fulfillment requests from in transit to in received
http http://localhost:8095/fulfillment/sample/receive
```

## Container Infrastructure

```text
CONTAINER ID        IMAGE                            COMMAND                  CREATED             STATUS              PORTS                                                NAMES
df8914058cbb        hlebalbau/kafka-manager:latest   "/kafka-manager/bin/…"   4 hours ago         Up 4 hours          0.0.0.0:9000->9000/tcp                               kafka-docker_kafka_manager_1
5cd8f61330e0        wurstmeister/kafka:latest        "start-kafka.sh"         4 hours ago         Up 4 hours          0.0.0.0:9092->9092/tcp                               kafka-docker_kafka_1
497901621c7d        mongo:latest                     "docker-entrypoint.s…"   4 hours ago         Up 4 hours          0.0.0.0:27017->27017/tcp                             kafka-docker_mongo_1
9079612e36ad        wurstmeister/zookeeper:latest    "/bin/sh -c '/usr/sb…"   4 hours ago         Up 4 hours          22/tcp, 2888/tcp, 3888/tcp, 0.0.0.0:2181->2181/tcp   kafka-docker_zookeeper_1
```

## Orders Customer Object in MongoDB

```text
docker exec -it kafka-docker_mongo_1 sh
mongo
use fulfillment
db.fulfillment.requests.find().pretty()
db.fulfillment.requests.remove({})
```

```bson
{
	"_id" : ObjectId("5b18878ca8d05609e6ff1851"),
	"timestamp" : NumberLong("1528334218838"),
	"name" : {
		"title" : "Ms.",
		"firstName" : "Susan",
		"lastName" : "Blackstone"
	},
	"contact" : {
		"primaryPhone" : "433-544-6555",
		"secondaryPhone" : "223-445-6767",
		"email" : "susan.m.blackstone@emailisus.com"
	},
	"address" : {
		"type" : "SHIPPING",
		"description" : "Home Sweet Home",
		"address1" : "33 Oak Avenue",
		"city" : "Nowhere",
		"state" : "VT",
		"postalCode" : "444556-9090"
	},
	"order" : {
		"guid" : "f52e2930-ef31-44db-a53c-b7ba4ae3f5cf",
		"orderStatusEvents" : [
			{
				"timestamp" : NumberLong("1528334457603"),
				"orderStatusType" : "COMPLETED"
			}
		],
		"orderItems" : [
			{
				"product" : {
					"guid" : "d01fde07-7c24-49c5-a5f1-bc2ce1f14c48",
					"title" : "Red Widget"
				},
				"quantity" : 2
			},
			{
				"product" : {
					"guid" : "4efe33a1-722d-48c8-af8e-7879edcad2fa",
					"title" : "Purple Widget"
				},
				"quantity" : 5
			}
		]
	},
	"shippingMethod" : "FedEx",
	"shippingStatusEvents" : [
		{
			"timestamp" : NumberLong("1528334457603"),
			"shippingStatusType" : "SHIPPED"
		},
		{
			"timestamp" : NumberLong("1528334457603"),
			"shippingStatusType" : "IN_TRANSIT"
		},
		{
			"timestamp" : NumberLong("1528334457603"),
			"shippingStatusType" : "RECEIVED"
		}
	],
	"_class" : "com.storefront.model.FulfillmentEvent"
}
```

## Current Results

Output from application, on the `orders.order.fulfill` topic

```text
2018-06-06 21:20:57.519  INFO [-,46fcaf5802394709,46fcaf5802394709,false] 2534 --- [nio-8095-exec-2] c.storefront.handler.AfterSaveListener   : onAfterSave event='org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent[source=FulfillmentRequest(id=5b18878ba8d05609e6ff1849, timestamp=1528334218543, name=Name(title=Mr., firstName=John, middleName=S., lastName=Doe, suffix=Jr.), contact=Contact(primaryPhone=555-666-7777, secondaryPhone=555-444-9898, email=john.doe@internet.com), address=Address(type=SHIPPING, description=My home address, address1=123 Oak Street, address2=null, city=Sunrise, state=CA, postalCode=12345-6789), order=Order(guid=ef8572ad-1e21-41ed-9f4d-7a908c0d0b9b, orderStatusEvents=[OrderStatusEvent(timestamp=1528334457507, orderStatusType=COMPLETED, note=null)], orderItems=[OrderItem(product=Product(guid=7f3c9c22-3c0a-47a5-9a92-2bd2e23f6e37, title=Green Widget), quantity=5), OrderItem(product=Product(guid=f3b9bdce-10d8-4c22-9861-27149879b3c1, title=Orange Widget), quantity=3), OrderItem(product=Product(guid=4efe33a1-722d-48c8-af8e-7879edcad2fa, title=Purple Widget), quantity=5)]), shippingMethod=FedEx, shippingStatusEvents=[ShippingStatusEvent(timestamp=1528334457507, shippingStatusType=SHIPPED, note=null), ShippingStatusEvent(timestamp=1528334457507, shippingStatusType=IN_TRANSIT, note=null), ShippingStatusEvent(timestamp=1528334457507, shippingStatusType=RECEIVED, note=null)])]'
2018-06-06 21:20:57.520  INFO [-,46fcaf5802394709,46fcaf5802394709,false] 2534 --- [nio-8095-exec-2] com.storefront.kafka.Sender              : sending payload='OrderStatusEventChange(guid=ef8572ad-1e21-41ed-9f4d-7a908c0d0b9b, orderStatusEvent=OrderStatusEvent(timestamp=1528334457507, orderStatusType=COMPLETED, note=null))' to topic='fulfillment.order.change'
2018-06-06 21:20:57.524  INFO [-,46fcaf5802394709,46fcaf5802394709,false] 2534 --- [nio-8095-exec-2] c.storefront.handler.AfterSaveListener   : onAfterSave event='org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent[source=FulfillmentRequest(id=5b18878ba8d05609e6ff184a, timestamp=1528334218805, name=Name(title=Ms., firstName=Mary, middleName=null, lastName=Smith, suffix=null), contact=Contact(primaryPhone=456-789-0001, secondaryPhone=456-222-1111, email=marysmith@yougotmail.com), address=Address(type=SHIPPING, description=Home Sweet Home, address1=1234 Main Street, address2=null, city=Anywhere, state=NY, postalCode=45455-66677), order=Order(guid=6d8a81e5-23dc-4ec5-8488-c7519c890ae1, orderStatusEvents=[OrderStatusEvent(timestamp=1528334457521, orderStatusType=COMPLETED, note=null)], orderItems=[OrderItem(product=Product(guid=b506b962-fcfa-4ad6-a955-8859797edf16, title=Black Widget), quantity=2), OrderItem(product=Product(guid=f3b9bdce-10d8-4c22-9861-27149879b3c1, title=Orange Widget), quantity=4)]), shippingMethod=FedEx, shippingStatusEvents=[ShippingStatusEvent(timestamp=1528334457521, shippingStatusType=SHIPPED, note=null), ShippingStatusEvent(timestamp=1528334457521, shippingStatusType=IN_TRANSIT, note=null), ShippingStatusEvent(timestamp=1528334457521, shippingStatusType=RECEIVED, note=null)])]'
2018-06-06 21:20:57.525  INFO [-,46fcaf5802394709,46fcaf5802394709,false] 2534 --- [nio-8095-exec-2] com.storefront.kafka.Sender              : sending payload='OrderStatusEventChange(guid=6d8a81e5-23dc-4ec5-8488-c7519c890ae1, orderStatusEvent=OrderStatusEvent(timestamp=1528334457521, orderStatusType=COMPLETED, note=null))' to topic='fulfillment.order.change'
2018-06-06 21:20:57.534  INFO [-,46fcaf5802394709,46fcaf5802394709,false] 2534 --- [nio-8095-exec-2] c.storefront.handler.AfterSaveListener   : onAfterSave event='org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent[source=FulfillmentRequest(id=5b18878ca8d05609e6ff184b, timestamp=1528334218806, name=Name(title=Ms., firstName=Susan, middleName=null, lastName=Blackstone, suffix=null), contact=Contact(primaryPhone=433-544-6555, secondaryPhone=223-445-6767, email=susan.m.blackstone@emailisus.com), address=Address(type=SHIPPING, description=Home Sweet Home, address1=33 Oak Avenue, address2=null, city=Nowhere, state=VT, postalCode=444556-9090), order=Order(guid=ffe75116-f72b-40c8-b819-b3cc0ad60b47, orderStatusEvents=[OrderStatusEvent(timestamp=1528334457525, orderStatusType=COMPLETED, note=null)], orderItems=[OrderItem(product=Product(guid=b506b962-fcfa-4ad6-a955-8859797edf16, title=Black Widget), quantity=2), OrderItem(product=Product(guid=f3b9bdce-10d8-4c22-9861-27149879b3c1, title=Orange Widget), quantity=2), OrderItem(product=Product(guid=d01fde07-7c24-49c5-a5f1-bc2ce1f14c48, title=Red Widget), quantity=5)]), shippingMethod=FedEx, shippingStatusEvents=[ShippingStatusEvent(timestamp=1528334457525, shippingStatusType=SHIPPED, note=null), ShippingStatusEvent(timestamp=1528334457525, shippingStatusType=IN_TRANSIT, note=null), ShippingStatusEvent(timestamp=1528334457525, shippingStatusType=RECEIVED, note=null)])]'
```

Output from Kafka container using the following command.

```bash
kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --from-beginning --topic orders.order.fulfill
```

Kafka Consumer Output

```text
{"timestamp":1528001014137,"name":{"title":"Mr.","firstName":"John","middleName":"S.","lastName":"Doe","suffix":"Jr."},"contact":{"primaryPhone":"555-666-7777","secondaryPhone":"555-444-9898","email":"john.doe@internet.com"},"address":{"type":"SHIPPING","description":"My home address","address1":"123 Oak Street","address2":null,"city":"Sunrise","state":"CA","postalCode":"12345-6789"},"order":{"guid":"617e9b8f-970c-487d-b4b7-faad870090c7","orderStatusEvents":[{"timestamp":1527996053859,"orderStatusType":"APPROVED","note":null}],"orderItems":[{"product":{"id":null,"guid":"f3b9bdce-10d8-4c22-9861-27149879b3c1","title":"Orange Widget","description":"Opulent Orange Widget","price":9.99},"quantity":3},{"product":{"id":null,"guid":"7f3c9c22-3c0a-47a5-9a92-2bd2e23f6e37","title":"Green Widget","description":"Gorgeous Green Widget","price":11.99},"quantity":4}]}}
{"timestamp":1528001014321,"name":{"title":"Ms.","firstName":"Mary","middleName":null,"lastName":"Smith","suffix":null},"contact":{"primaryPhone":"456-789-0001","secondaryPhone":"456-222-1111","email":"marysmith@yougotmail.com"},"address":{"type":"SHIPPING","description":"Home Sweet Home","address1":"1234 Main Street","address2":null,"city":"Anywhere","state":"NY","postalCode":"45455-66677"},"order":{"guid":"d51113cd-fbc1-45b3-b3e9-53b78d22b5bf","orderStatusEvents":[{"timestamp":1527996053859,"orderStatusType":"APPROVED","note":null}],"orderItems":[{"product":{"id":null,"guid":"a9d5a5c7-4245-4b4e-b1c3-1d3968f36b2d","title":"Yellow Widget","description":"Amazing Yellow Widget","price":5.99},"quantity":1},{"product":{"id":null,"guid":"4efe33a1-722d-48c8-af8e-7879edcad2fa","title":"Purple Widget","description":"Pretty Purple Widget","price":7.99},"quantity":4},{"product":{"id":null,"guid":"a9d5a5c7-4245-4b4e-b1c3-1d3968f36b2d","title":"Yellow Widget","description":"Amazing Yellow Widget","price":5.99},"quantity":4}]}}
{"timestamp":1528001014339,"name":{"title":"Ms.","firstName":"Susan","middleName":null,"lastName":"Blackstone","suffix":null},"contact":{"primaryPhone":"433-544-6555","secondaryPhone":"223-445-6767","email":"susan.m.blackstone@emailisus.com"},"address":{"type":"SHIPPING","description":"Home Sweet Home","address1":"33 Oak Avenue","address2":null,"city":"Nowhere","state":"VT","postalCode":"444556-9090"},"order":{"guid":"05f3d73c-df01-4b31-87b0-3b65065222bd","orderStatusEvents":[{"timestamp":1527996053859,"orderStatusType":"APPROVED","note":null}],"orderItems":[{"product":{"id":null,"guid":"d01fde07-7c24-49c5-a5f1-bc2ce1f14c48","title":"Red Widget","description":"Reliable Red Widget","price":3.99},"quantity":4}]}}
```

The `fulfillment.order.change` sends fulfilled order notifications back to orders, via topic

```bash
kafka-topics.sh --create \
  --zookeeper zookeeper:2181 \
  --replication-factor 1 --partitions 1 \
  --topic fulfillment.order.change

kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --from-beginning --topic fulfillment.order.change
```

Output from application, on the `fulfillment.order.change` topic

```text
{"guid":"5f900d92-e2a2-484f-8e9c-7e0a24b093fd","orderStatusEvent":{"timestamp":1528334452755,"orderStatusType":"PROCESSING","note":null}}
{"guid":"f52e2930-ef31-44db-a53c-b7ba4ae3f5cf","orderStatusEvent":{"timestamp":1528334452800,"orderStatusType":"PROCESSING","note":null}}
{"guid":"ef8572ad-1e21-41ed-9f4d-7a908c0d0b9b","orderStatusEvent":{"timestamp":1528334457507,"orderStatusType":"COMPLETED","note":null}}
{"guid":"6d8a81e5-23dc-4ec5-8488-c7519c890ae1","orderStatusEvent":{"timestamp":1528334457521,"orderStatusType":"COMPLETED","note":null}}
{"guid":"ffe75116-f72b-40c8-b819-b3cc0ad60b47","orderStatusEvent":{"timestamp":1528334457525,"orderStatusType":"COMPLETED","note":null}}
```

## References

-   [Spring Kafka – Consumer and Producer Example](https://memorynotfound.com/spring-kafka-consume-producer-example/)
-   [Spring Kafka - JSON Serializer Deserializer Example](https://www.codenotfound.com/spring-kafka-json-serializer-deserializer-example.html)
-   [Spring for Apache Kafka: 2.1.6.RELEASE](https://docs.spring.io/spring-kafka/reference/html/index.html)
-   [Spring Data MongoDB - Reference Documentation](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/)

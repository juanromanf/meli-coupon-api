# coupon-api

This is the solution for the coupon API challenge using Java + Spring boot.

To run this you will need:
- Java 11+
- Maven 3.6+
- Your favorite IDE

### How to run

To run this project on your local machine:
```
mvn spring-boot:run
```

Then check if the project it is running using the following URL:

```
GET http://localhost:9090/actuator/health
```

### Coverage

To check the code coverage report run:
```
mvn clean test
```
And then check the report under the folder:
```
target/site/jacoco/index.html
```

### Testing the endpoint

Local endpoint URL:

```
POST http://localhost:9090/coupon
```

Sample payload
```
curl --location --request POST 'http://localhost:9090/coupon' \
--header 'Content-Type: application/json' \
--data-raw '{
    "item_ids": [
        "MLA811601010",
        "MLA811601011",
        "MLA811601058",
        "MLA811601056",
        "MLA811601014",
        "MLA811601055",
        "MLA811601004",
        "MLA811602004",
        "MLA811601018",
        "MLA811602000",
        "MLA811602007",
        "MLA811601002",
        "MLA811601022",
        "MLA811602009",
        "MLA811601008",
        "MLA811601025",
        "MLA811601003",
        "MLA811601027",
        "MLA811601028",
        "MLA811602012",
        "MLA811601050",
        "MLA811601090",
        "MLA811601008"
    ],
    "amount": 5000
}'
```

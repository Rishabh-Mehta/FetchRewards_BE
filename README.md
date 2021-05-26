# Fetch Rewards Backend Engineer - Coding Exercise 

The project is built using Spring Boot and uses JPA to implement database operations.
## Prerequisites 
    * Java 11
    * Maven
    * Intellij/Eclipse
    * PostMan (Optional)
## Usage
 * From Project root
```
$ mvn spring-boot:run
```
(OR)
* Open project in Intellij/Eclipse and run. 

* Use Postman (or) Curl from terminal to interact with the application

## Sample Output 
    * Output for sample data shown in the points.pdf has been saved in Test.postman_collection.json
    * Test.postman_collection.json can be imported to postman to run individual commands.



## Components 

### Payer
This Model defines 
   ```java
@Id
@GeneratedValue(generator = "PAYER_SEQ")
private long id;
String payer;
int points;
   ```
* @Id annotation automatically generates an Id for every new Payer Entity.

### Transaction

This Model Defines 
```java
@Id
@GeneratedValue(generator = "TRANSACTION_SEQ")
private long id;   
private long transaction_reedemed_id;
private UUID redeem_request_id;
private String payer;
private int awarded_points;
private int available_points;
private LocalDateTime timestamp;
@UpdateTimestamp
private LocalDateTime updatetimestamp;
private String status;
   ```
* @Id annotation automatically generates an Id for every new Transaction Entity.

* Transaction Entity has been slightly expanded than the given requirements.

* transaction_reedemed_id - Tracks which transaction is used to reedem a request 

* redeem_request_id - Creates a redeem_request_id of type UUID for each redeem request and is used to identify the redeem transactions if redeem points span across multiple transactions

* awarded_points - Shows awarded/redeemed points

* available_points - Shows available points for redemption

* status - AVAILABLE,PROCESSED,FAIL these types define the transaction status.
  * If AVAILABLE transaction still has some points that can be redeemed.
  * If PROCESSED transaction all points in the transaction have been processed
  * If FAIL this shows the redeem transaction failed due to INSUFFICIENT_POINTS.

### Routes 

* #### **/payer/balances**
    * Request 
        ```
      curl -L -X GET "http://localhost:8080/payer/balances/"
      ```
    * Input - None
    * Output - JSON 
      ```JSON
      [
          {
          "id": 1,
          "payer": "CHASE",
          "points": 200
          }
      ]
      ```
    * Shows Payer points 

* #### **/payer/transaction/add**
    * Request - 
        ```
      curl -L -X POST "localhost:8080/payer/transaction/add" -H "Content-Type: application/json" --data-raw "{\"payer\":\"CHASE\",\"awarded_points\": 200,\"timestamp\":\"2020-05-26T11:00:00Z\"}"
        ```
      
    * Input - JSON Body
        ```JSON
      {"payer":"CHASE",
      "awarded_points": 200,
      "timestamp":"2020-05-26T11:00:00Z"}

      ```
      * Output - JSON
      ```JSON
      {
      "id": 1,
      "transaction_reedemed_id": 0,
      "redeem_request_id": null,
      "payer": "CHASE",
      "awarded_points": 200,
      "available_points": 200,
      "timestamp": "2020-05-26T11:00:00",
      "status": "AVAILABLE"
      }
      ```
    * Adds a transaction with specified payer, points and timestamp.
    * If Payer is not already added creates a new payer entity with given points.

* #### **/payer/transaction/redeem**
    * Request 
      ```
      curl -L -X POST "localhost:8080/payer/transaction/redeem" -H "Content-Type: application/json" --data-raw "{\"points\":100}"
      ```
    * Input  -JSON Body
      ```JSON
      {"points":100}
      ```

    * Output -JSON
  ```JSON
  [
    {
        "id": 2,
        "transaction_reedemed_id": 1,
        "redeem_request_id": "d3053ea3-9af1-4dde-ae2f-b1b6675e2c3b",
        "payer": "CHASE",
        "awarded_points": -100,
        "available_points": 0,
        "timestamp": "2021-05-26T17:30:10.8466324",
        "status": "REDEEM"
    }
]
     ```
    * Redeems the specified points from the transaction list with AVAILABLE status in ascending order of the timestamp.
    * Each Redeem transaction is assigned a UUID, which helps in tracking a redeem request if multiple AVAILABLE transactions are used to fulfill the redeem request.
    * Redeem Request can also fail if total available points are less than the requested.
* #### **/transaction/logs**
    * Request 
      ```
      curl -L -X GET "http://localhost:8080/transactions/log/"
      ```
    * Input - None 
    * Output - JSON
      ```JSON
      [
          {
          "id": 1,
          "transaction_reedemed_id": 0,
          "redeem_request_id": null,
          "payer": "CHASE",
          "awarded_points": 200,
          "available_points": 200,
          "timestamp": "2020-05-26T11:00:00",
          "status": "AVAILABLE"
          }
      ]
      ```
    * Shows Transaction Details
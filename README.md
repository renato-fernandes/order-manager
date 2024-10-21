## Order Manager app

---

> This is a simple exercise, a simple order manager. An API where users can create and manage orders. Items can be ordered and orders are automatically fulfilled as soon as the item stock allows it.

### Specification:
The system should be able to provide the following features:
- [x] create, read, update and delete and list all entities;
- [x] when an order is created, it should try to satisfy it with the current stock.;
- [x] when a stock movement is created, the system should try to attribute it to an order that isn't complete;
- [x] when an order is complete, send a notification by email to the user that created it;
- [x] trace the list of stock movements that were used to complete the order, and vice-versa;
- [x] show current completion of each order;
- [x] Write a log file with: orders completed, stock movements, email sent and errors.

### Entities:

| items         |  |  |
|---------------|-------|------------|
| id_item       | serial  | primary key |
| name          | varchar(100)  | not null   |
| current_stock | int  | not null   |

| users   |  |  |
|---------|-------|------------|
| id_user | serial  | primary key |
| name    | varchar(100)  | not null   |
| email   | varchar(255)  | not null   |

| orders        |  |             |
|---------------|-------|-------------|
| id_order      | serial  | primary key |
| creation_date | timestamp  | not null    |
| quantity      | int | not null    |
| item_id       | int | not null, foreign key(id_item)  |
| user_id       | int | not null, foreign key(id_user)  |
| is_complete   | smallint |   |

| stock_movements        |  |             |
|---------------|-------|-------------|
| id_stock      | serial  | primary key |
| creation_date | timestamp  | not null    |
| quantity      | int | not null    |
| item_id       | int | not null, foreign key(id_item)  |
| order_id       | int | not null, foreign key(id_order)  |

### Requirements:
- [x] java 8 (java version "1.8.0_202")
- [x] Hibernate (JPA) 
- [x] Maven
- [x] Docker
- [x] PostgreSQL
- [x] log4j

---

## How to use the app:

### Environment setup:

1. Setup java JDK 8 or above
2. Install Docker Desktop and run it
3. after Checking out this project you can open it with an IDE (Intellij preferable)
4. Build the JAR file with Maven
5. run the app with the IDE or the builded JAR file:

`java -jar order-manager-1.0-SNAPSHOT.jar`

### Api Endpoints:

When running app locally, you can use it by the following endpoints and it's usage:

#### Items
createItem
- HTTP Method: POST
- local endpoint: http://localhost:8080/item/create
- payload (body) example:
``` 
{
    "name": "camisa",
    "currentStock": 40
}
```
- curl example:

``` curl
curl --location 'http://localhost:8080/item/create' \
      --header 'Content-Type: application/json' \
      --data '{
      "name": "camisa",
      "currentStock": 40
      }'
```
---
getAllItems
- HTTP Method: GET
- local endpoint: http://localhost:8080/item/retrieve
- curl example:
``` 
curl --location 'http://localhost:8080/item/retrieve/'
```
---
getItem
- HTTP Method: GET
- local endpoint: http://localhost:8080/item/retrieve/{name}
- curl example:
``` curl
curl --location 'http://localhost:8080/item/retrieve/camisa'
```
---
updateItem
- HTTP Method: PUT
- local endpoint: http://localhost:8080/item/update
- payload (body) example:
``` 
{
    "id": 1,
    "name": "camisa social",
    "currentStock": 50
}
```
- curl example:

``` curl
curl --location --request PUT 'http://localhost:8080/item/update' \
--header 'Content-Type: application/json' \
--data '{
    "id": 1,
    "name": "camisa social",
    "currentStock": 50
}'
```
---
deleteItem
- HTTP Method: DELETE
- local endpoint: http://localhost:8080/item/delete
- payload (body) example:
``` 
{
    "id": 1
}
```
- curl example:

``` curl
curl --location --request DELETE 'http://localhost:8080/item/delete' \
--header 'Content-Type: application/json' \
--data '{
    "id": 1
}'
```
---

---

#### Users
createUser
- HTTP Method: POST
- local endpoint: http://localhost:8080/user/create
- payload (body) example: 
> ***IMPORTANT:*** use a **valid** email account to receive email notification from app
``` 
{
    "name": "Pedro",
    "email": "something@gmail.com"
}
```
- curl example:

``` curl
curl --location 'http://localhost:8080/user/create' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Pedro",
    "email": "something@gmail.com"
}'
```

---
getAllUsers
- HTTP Method: GET
- local endpoint: http://localhost:8080/user/retrieve
- curl example:
``` 
curl --location 'http://localhost:8080/user/retrieve'
```
---
getUser
- HTTP Method: GET
- local endpoint: http://localhost:8080/user/retrieve/{email}
> ***IMPORTANT:*** currently app only searches users by email account (username)
- curl example:
``` curl
curl --location 'http://localhost:8080/user/retrieve/something@gmail.com'
```
---
updateUser
- HTTP Method: PUT
- local endpoint: http://localhost:8080/user/update
- payload (body) example:
``` 
{
    "id": 1,
    "name": "Pedro Filipe",
    "email": "pedrofilipe@gmail.com"
}
```
- curl example:

``` curl
curl --location --request PUT 'http://localhost:8080/user/update' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": 1,
    "name": "Pedro Filipe",
    "email": "pedrofilipe@gmail.com"
}'
```
---
deleteUser
- HTTP Method: DELETE
- local endpoint: http://localhost:8080/user/delete
- payload (body) example:
``` 
{
    "id": 1
}
```
- curl example:

``` curl
curl --location --request DELETE 'http://localhost:8080/user/delete' \
--header 'Content-Type: application/json' \
--data '{
    "id": 1
}'
```


---

---

#### Orders
createOrder
- HTTP Method: POST
- local endpoint: http://localhost:8080/order/create
- payload (body) example:
``` 
{
    "quantity": 200,
    "item": {"id": 1},
    "user": {"id": 1}
}
```
- curl example:

``` curl
curl --location 'http://localhost:8080/order/create' \
--header 'Content-Type: application/json' \
--data '{
    "quantity": 200,
    "item": {"id": 1},
    "user": {"id": 1}
}'
```

---
getAllOrders
- HTTP Method: GET
- local endpoint: http://localhost:8080/order/retrieve
- curl example:
``` 
curl --location 'http://localhost:8080/order/retrieve'
```
---
getOrder
- HTTP Method: GET
- local endpoint: http://localhost:8080/order/retrieve/{id}
- curl example:
``` curl
curl --location 'http://localhost:8080/order/retrieve/1'
```
---
updateOrder
- HTTP Method: PUT
- local endpoint: http://localhost:8080/order/update
> ***IMPORTANT:*** You can only update an incomplete order
- payload (body) example:
> ***ATTENTION:*** you need a full Order object Json to update it. be sure to get all fields before trying to update
``` 
{
    "id": 1,
    "creationDate": {current creation date in Instant format}
    "quantity": 300,
    "isComplete": false
    "item": {"id": 1},
    "user": {"id": 1}
}
```
- curl example:

``` curl
curl --location --request PUT 'http://localhost:8080/order/update' \
--header 'Content-Type: application/json' \
--data '{
    "id": 1,
    "creationDate": {current creation date in Instant format}
    "quantity": 300,
    "isComplete": false
    "item": {"id": 1},
    "user": {"id": 1}
}'
```
---
deleteOrder
- HTTP Method: DELETE
- local endpoint: http://localhost:8080/order/delete
- payload (body) example:
``` 
{
    "id": 1
}
```
- curl example:

``` curl
curl --location --request DELETE 'http://localhost:8080/order/delete' \
--header 'Content-Type: application/json' \
--data '{
    "id": 1
}'
```


---

---

#### StockMovements
createStock
- HTTP Method: POST
- local endpoint: http://localhost:8080/stock/create
> ***IMPORTANT:*** A stock Movemente will be automatically associated to the oldest not completed Order for the same Item it refers to
- payload (body) example:
``` 
{
    "quantity": 60,
    "item": {"id": 1}
}
```
- curl example:

``` curl
curl --location 'http://localhost:8080/stock/create' \
--header 'Content-Type: application/json' \
--data '{
    "quantity": 60,
    "item": {"id": 1}
}'
```

---
getAllStocks
- HTTP Method: GET
- local endpoint: http://localhost:8080/stock/retrieve
- curl example:
``` 
curl --location 'http://localhost:8080/stock/retrieve'
```
---
getStock
- HTTP Method: GET
- local endpoint: http://localhost:8080/stock/retrieve/{id}
- curl example:
``` curl
curl --location 'http://localhost:8080/stock/retrieve/1'
```
---
updateStock
- HTTP Method: PUT
- local endpoint: http://localhost:8080/stock/update
> ***IMPORTANT:*** You can only update a quantity for a stockMovement associated to an incomplete order
- payload (body) example:
> ***ATTENTION:*** you need a full StockMovement object Json to update it. be sure to get all fields before trying to update
``` 
{
    "id": 1,
    "creationDate": {current creation date in Instant format}
    "quantity": 80,
    "item": {"id": 1},
    "order": {"id": 1}
}
```
- curl example:

``` curl
curl --location --request PUT 'http://localhost:8080/stock/update' \
--header 'Content-Type: application/json' \
--data '{
    "id": 1,
    "creationDate": {current creation date in Instant format}
    "quantity": 80,
    "item": {"id": 1},
    "order": {"id": 1}
}'
```
---
deleteStock
- HTTP Method: DELETE
- local endpoint: http://localhost:8080/stock/delete
> ***IMPORTANT:*** You can only delete stockMovement associated to an incomplete order
- payload (body) example:
``` 
{
    "id": 1
}
```
- curl example:

``` curl
curl --location --request DELETE 'http://localhost:8080/stock/delete' \
--header 'Content-Type: application/json' \
--data '{
    "id": 1
}'
```
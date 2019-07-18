# MyMoney
A simple REST API for transfer money between accounts

Condition: No use of *Spring-Framework*

Based on Java 11

## Frameworks
* Guice
* Javalin
* Hibernate
* H2 DB
* Lombok
* Gson
* Mockito
* Maven


## Usage

```bash
> mvn clean install

> java -jar target/standalone.jar

```
Server will start at **http://localhost:8080/**

## Services

#### Account

* (POST) /mymoney/service/account - Creates an Account. AccountRequest as json string in body. Possible currencies see **[Currencies](https://github.com/eugen-cc/mymoney/blob/master/src/main/java/cc/eugen/mymoney/model/entity/Currency.java)** 

```bash
curl -XPOST http://localhost:8080/mymoney/service/account -d "{'balance':300.50, 'currency':'EUR'}"
```
* (GET) /mymoney/service/account/:id - retrieves an account by id

```bash
curl http://localhost:8080/mymoney/service/account/42"
```



#### Transfer money

* (POST) /mymoney/service/transfer - Creates a transfer. The amount has the currency of the sender.

```bash
curl -XPOST localhost:8080/mymoney/service/transfer -d "{'sender':4, 'receiver':1, 'amount':34.55}"
```

#### Transactions

* (GET) /mymoney/service/transactions - Retrieves a list of all transactions

Profidata OrderBook Exercise
==============================

Your task is to create a small FX OrderBook application that communicates with a given order service. 
The application should be a Java CLI application that reads commands from stdin, processes them and prints the results to stdout.
The communication between your application and the order service is based on HTTP. 
The libraries/frameworks/IDEs you use to complete the task are completely up to you. 
The only constraint is that you should provide a solution that can be built and executed with JDK21 or higher. 

Please provide a solution that fulfill the requirements described below and that you consider it production ready from every aspect you can think of.
The solution (with source code) should be provided back to us in a zip or tar package (eventually with a quick note on how to build and run it).

Requirements - your application must support these commands: 
------------------------------------------------------------

* new [buy|sell] <investment ccy> <counter ccy> <limit> <validity>
  Example: "new buy EUR CHF 1.14 31.12.2025"
  This command should create a new order in the order book and print the ID of the newly created order.

* cancel <ID>
  Example: "cancel 5"
  This command should cancel the order with the given ID.

* rates
  This command should print an overview of the current exchange rates.

* orders
  This command should print an overview of all orders currently in the order book. 
  The orders should be sorted by currency pair and the distance to the current market rate (which should also be printed out). 
  So, if we would have the following 3 orders:
      1: buy  EUR USD 1   31.12.2018
      2: sell EUR USD 1.2 31.12.2018
      3: buy  EUR USD 1.4 31.12.2018
  and the current EUR/USD exchange rate is 1.5, then the expected result from invoking "orders" should look something like this: 
      buy    EUR   USD   1.4   31.12.2018  0.1
      sell   EUR   USD   1.2   31.12.2018  0.3
      buy    EUR   USD   1     31.12.2018  0.5

* summary
  This command should calculate a summary of the current order book.
  Orders should be grouped and sorted by investment ccy, counter currency and buy/sell.
  And you need to show also the number of orders and the average limit. 
  Imagine, we currently have the following orders in the book: 
      1: buy  EUR USD 1     31.12.2018
      2: sell EUR CHF 1.15  31.12.2019
      3: sell EUR CHF 1.17  31.12.2018
      4: sell EUR USD 2     01.01.2019
  Invoking "summary" should then print something like this:
      sell   EUR   CHF   2   1.16
      buy    EUR   USD   1   1
      sell   EUR   USD   1   2


The order service
-------------------------------------------------------------------------------------
You can find the order service to communicate with in the sub-folder "order-service". 
It is packaged in an executable jar file. 
The jar can be run with JDK21 or newer. 
The API endpoints exposed by that service are documented in the file openapi.yaml (located in the same directory).

The order service stores its data locally in the files "fxRates.ser" and "orders.ser" (they will be available on the current directory from where you execute the order-service.jar after first run). 
If you need to reset your state (start with initial data) then simply stop the server, delete the two files and then start the order service again.

Please do not distribute this challenge or the solution to anybody else!
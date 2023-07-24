# Introduction
This training project provides code samples for interacting with the PSQL relational database using JDBC PSQL driver external dependency managed by the Maven build automation tool. The database interaction logic has been encapsulated within a CustomerDAO class that implements the Data Access Object pattern. That class functionality includes CRUD methods such as create, read, update and delete. The Data Transfer Object class named Customer was designed to encapsulate customer table data.

# Implementation
The Project was implemented using a database-first approach. This involved executing SQL scripts in util/db_preparation_scripts  to create a database and tables. Those scripts created five tables: customers, orders, order_item, product and sales_person. Afterwards, test data was inserted into these tables.

Once the database part was completed, the code part implementation started. First, a new Maven project was configured with a PSQL driver external dependency to communicate with the database. Additionally, Lombok was added to reduce boilerplate code. Next, DAO and DTO interfaces were created and implemented by CustomerDAO and Customer classes. Finally, these classes were utilized for basic CRUD operations in the JDBCExecutor class.

# ER Diagram
![er_diagram.png](src%2Fresources%2Fer_diagram.png)
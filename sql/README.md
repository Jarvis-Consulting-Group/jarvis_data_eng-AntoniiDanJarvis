# Introduction
This project is dedicated to practising SQL queries and contains 28 solved tasks. The difficulty range of tasks varies greatly, from simplest select all to triple joins.

## Table Setup (DDL)
To set up DB was used sql/clubdata.sql script.
```
create table members (
    memid integer,
    surname character varying(200),
    firstname character varying(200),
    address character varying(300),
    zipcode integer,
    telephone character varying(20),
    recommendedby integer,
    joindate timestamp,
    primary key (memid),
    foreign key (recommendedby) references members (memid)
);
create table bookings (
    facid integer,
    memid integer,
    starttime timestamp,
    slots integer,
    primary key (facid),
    foreign key (memid) references members (memid)
);
create table facilities (
    facid integer,
    name character varying(100),
    membercost numeric,
    guestcost numeric,
    initialoutlay numeric,
    monthlymaintenance numeric,
    primary key (facid)
);
```

## SQL Queries

### Task 1: Insert data into a table
```sql
insert into cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
values (9,'Spa', 20, 30, 100000, 800.);
```

### Task 2: Insert calculated data into a table

```sql
insert into cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
values ((select (max(facid)+1) from cd.facilities),'Spa', 20, 30, 100000, 800.);
```

### Task 3: Update existing data

```sql
update cd.facilities
set initialoutlay=10000
where name='Tennis Court 2';
```

### Task 4: Update a row based on the contents of another row

```sql
update cd.facilities
set guestcost=((select guestcost from cd.facilities where name='Tennis Court 1')* 1.1),
membercost=((select membercost from cd.facilities where name='Tennis Court 1')* 1.1)
where name='Tennis Court 2';
```

### Task 5: Delete all

```sql
delete from cd.bookings;
```

### Task 6: Delete a member from table

```sql
delete from cd.members where memid=37;
```

### Task 7: Control which rows are retrieved

```sql
select facid, name, membercost, monthlymaintenance
from cd.facilities
where membercost > 0 and membercost < (monthlymaintenance / 50);
```

### Task 8: Basic string searches

```sql
select * from cd.facilities where name~'Tennis';
```

### Task 9: Matching against multiple possible values

```sql
select * from cd.facilities where facid in(1,5);
```

### Task 10: Working with dates

```sql
select memid, surname, firstname, joindate from cd.members where joindate > '2012-08-31';
```

### Task 11: Combining results from multiple queries

```sql
select surname from cd. members union
select name from cd.facilities;
```

### Task 12: Retrieve the start times of members' bookings

```sql
select cd.bookings.starttime from cd.bookings
inner join cd.members
on cd.bookings.memid=cd.members.memid
where cd.members.firstname='David' and cd.members.surname='Farrell';
```

### Task 13: Work out the start times of bookings for tennis courts

```sql
select cd.bookings.starttime, cd.facilities.name
from cd.bookings
left join cd.facilities
on cd.bookings.facid = cd.facilities.facid
where cd.bookings.starttime > '2012-09-21 00:00:00' and cd.bookings.starttime < '2012-09-22 00:00:00'
and cd.facilities.name ~ 'Tennis Court'
order by cd.bookings.starttime;
```

### Task 14: Produce a list of all members, along with their recommender

```sql
select A.firstname, A.surname, B.firstname, B.surname
from cd.members A
left outer join cd.members B
on A.recommendedby = B.memid
order by A.surname, A.firstname;
```

### Task 15: Produce a list of all members who have recommended another member

```sql
select distinct B.firstname, B.surname
from cd.members A, cd.members B
where A.recommendedby = B.memid
order by B.surname, B.firstname;
```

### Task 16: Produce a list of all members, along with their recommender, using no joins

```sql
select distinct (A.firstname || ' ' || A.surname) as member,
(select (B.firstname || ' ' || B.surname) as recommender
from cd.members B
where A.recommendedby = B.memid)
from cd.members A
order by member;
```

### Task 17: Count the number of recommendations each member makes

```sql
select recommendedby, count(memid)
from cd.members
where recommendedby is not null
group by recommendedby
order by recommendedby;
```

### Task 18: List the total slots booked per facility

```sql
select facid, sum(slots)
from cd.bookings
group by facid
order by facid;
```

### Task 19: List the total slots booked per facility in a given month

```sql
select facid, sum(slots) as "Total Slots"
from cd.bookings
where starttime > '2012-09-01 00:00:00' and starttime < '2012-10-01 00:00:00'
group by facid
order by "Total Slots";
```

### Task 20: List the total slots booked per facility per month

```sql
select facid, extract(month from starttime) as mounth, sum(slots) as "Total Slots"
from cd.bookings
where starttime > '2012-01-01 00:00:00' and starttime < '2013-01-01 00:00:00'
group by facid, mounth
order by facid, mounth;
```

### Task 21: Find the count of members who have made at least one booking

```sql
select count(memid) from (select distinct memid from cd.bookings) as A;
```

### Task 22: List each member's first booking after September 1st 2012

```sql
select mem.surname, mem.firstname, book.memid, min(book.starttime) from cd.members as mem
left join cd.bookings as book
on mem.memid = book.memid
where starttime > '2012-09-01 00:00:00'
group by mem.firstname, mem.surname, book.memid
order by book.memid;
```

### Task 23: Produce a list of member names, with each row containing the total member count

```sql
select (select count(memid) from cd.members), firstname, surname from cd.members;
```

### Task 24: Produce a numbered list of members

```sql
select row_number() over (order by joindate), firstname, surname from cd.members;
```

### Task 25: Output the facility id that has the highest number of slots booked, again select facid, sum(slots) as total

```sql
from cd.bookings
group by facid
order by total desc
limit 1;
```

### Task 26: Format the names of members

```sql
select (surname || ', ' || firstname) as name from cd.members;
```

### Task 27: Find telephone numbers with parentheses

```sql
select memid, telephone from cd.members where telephone ~ '^[(][0-9]{3}[)]';
```

### Task 28: Count the number of members whose surname starts with each letter of the alphabet

```sql
select A.letter, count(A.letter) as count
from (select substr(surname, 1, 1) as letter from cd.members) as A
group by A.letter
order by A.letter;
```
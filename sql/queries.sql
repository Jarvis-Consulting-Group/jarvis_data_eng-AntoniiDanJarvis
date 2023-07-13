--1) Insert data into a table
 insert into cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
 values (9,'Spa', 20, 30, 100000, 800.);
--2) Insert calculated data into a table
 insert into cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
 values ((select (max(facid)+1) from cd.facilities),'Spa', 20, 30, 100000, 800.);
--3) Update existing data
 update cd.facilities
 set initialoutlay=10000
 where name='Tennis Court 2';
 --4) Update a row based on the contents of another row
 update cd.facilities
 set guestcost=((select guestcost from cd.facilities where name='Tennis Court 1')* 1.1),
 	 membercost=((select membercost from cd.facilities where name='Tennis Court 1')* 1.1)
 where name='Tennis Court 2';
 --5) Delete all
 delete from cd.bookings;
 --6) Delete a member from table
 delete from cd.members where memid=37;
 --7) Control which rows are retrieved
 select facid, name, membercost, monthlymaintenance
 from cd.facilities
 where membercost > 0 and membercost < (monthlymaintenance / 50);
 --8) Basic string searches
 select * from cd.facilities where name~'Tennis';
 --9) Matching against multiple possible values
 select * from cd.facilities where facid in(1,5);
 --10) Working with dates
select memid, surname, firstname, joindate from cd.members where joindate > '2012-08-31';
--11) Combining results from multiple queries
select surname from cd. members union
select name from cd.facilities;
--12) Retrieve the start times of members' bookings
select cd.bookings.starttime from cd.bookings
inner join cd.members
on cd.bookings.memid=cd.members.memid
where cd.members.firstname='David' and cd.members.surname='Farrell';
--13) Work out the start times of bookings for tennis courts
select cd.bookings.starttime, cd.facilities.name
from cd.bookings
left join cd.facilities
on cd.bookings.facid = cd.facilities.facid
where cd.bookings.starttime > '2012-09-21 00:00:00' and cd.bookings.starttime < '2012-09-22 00:00:00'
and cd.facilities.name ~ 'Tennis Court'
order by cd.bookings.starttime;
--14) Produce a list of all members, along with their recommender
select A.firstname, A.surname, B.firstname, B.surname
from cd.members A
left outer join cd.members B
on A.recommendedby = B.memid
order by A.surname, A.firstname;
--15) Produce a list of all members who have recommended another member
select distinct B.firstname, B.surname
from cd.members A, cd.members B
where A.recommendedby = B.memid
order by B.surname, B.firstname;
--16) Produce a list of all members, along with their recommender, using no joins.
select distinct (A.firstname || ' ' || A.surname) as member,
(select (B.firstname || ' ' || B.surname) as recommender
 from cd.members B
 where A.recommendedby = B.memid)
from cd.members A
order by member;
--17) Count the number of recommendations each member makes.
select recommendedby, count(memid)
from cd.members
where recommendedby is not null
group by recommendedby
order by recommendedby;
--18) List the total slots booked per facility
select facid, sum(slots)
from cd.bookings
group by facid
order by facid;
--19) List the total slots booked per facility in a given month
select facid, sum(slots) as "Total Slots"
from cd.bookings
where starttime > '2012-09-01 00:00:00' and starttime < '2012-10-01 00:00:00'
group by facid
order by "Total Slots";
--20) List the total slots booked per facility per month
select facid, extract(month from starttime) as mounth, sum(slots) as "Total Slots"
from cd.bookings
where starttime > '2012-01-01 00:00:00' and starttime < '2013-01-01 00:00:00'
group by facid, mounth
order by facid, mounth;
--21) Find the count of members who have made at least one booking
select count(memid) from (select distinct memid from cd.bookings) as A;
--22) List each member's first booking after September 1st 2012
select mem.surname, mem.firstname, book.memid, min(book.starttime) from cd.members as mem
left join cd.bookings as book
on mem.memid = book.memid
where starttime > '2012-09-01 00:00:00'
group by mem.firstname, mem.surname, book.memid
order by book.memid;
--23) Produce a list of member names, with each row containing the total member count
select (select count(memid) from cd.members), firstname, surname from cd.members;
--24) Produce a numbered list of members
select row_number() over (order by joindate), firstname, surname from cd.members;
--25) Output the facility id that has the highest number of slots booked, again
select facid, sum(slots) as total
from cd.bookings
group by facid
order by total desc
limit 1;
--26) Format the names of members
select (surname || ', ' || firstname) as name from cd.members;
--27) Find telephone numbers with parentheses
select memid, telephone from cd.members where telephone ~ '^[(][0-9]{3}[)]';
--28) Count the number of members whose surname starts with each letter of the alphabet
select A.letter, count(A.letter) as count
from (select substr(surname, 1, 1) as letter from cd.members) as A
group by A.letter
order by A.letter;
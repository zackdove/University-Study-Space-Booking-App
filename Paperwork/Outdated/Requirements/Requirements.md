//Requirements//

/Stakeholders/

Rahim Ahmed - Faculty Office Manager for the Engineering Department -
The space booking app was requested by Rahim, and he laid out his own specification. Although Rahim is not and end-user, he must approve of the application and is responsible for it in the University. His priorities are in student and admin ease of use.

UoB Engineering Undergrads -
These students form one side of the main end users. They themselves will be using the app to book the room.

UoB Engineering Office Admins / Librarians -
These are the other side of the end users. They will use the app to monitor and manage the bookings, aiding the students and the process.

Chris Totthill - IT Manager for the Engineering Department -
Chris is in charge of implementation and purchase of new applications, and his approval is also needed.

University IT and Maintenance Teams -
They will be in charge of maintaining and updating the application post release. They will also deal with students needing help.

/High Level Use Case Diagrams/

See image.

/Functional Requirements/

* To be able book one of many study space rooms in the Engineering department, the core of the project.
* To log in with Single Sign On, fitting in with other University applications and increasing speed and ease of use for users
* To see the availability of rooms before booking, enabling users to plan ahead
* To have the ability to cancel bookings, both for users and admins
* For students to view their current bookings, and admins to view all bookings for management
* To be able to export bookings to eCalendars to improve ease of use for users
* For fairness and wellbeing, students will only be able to book up to 2 hours per day (10 a week)
* All required information should be stored on a database
* Have a separate version for administrators, enabling them to manage the system
* There should be a page to show statistics about the use of the application, viewable by administrators only
* Administrators should be able to blacklist students
* Students not from the Engineering department should not be allowed to book rooms
* Have a terms and conditions page, highlighting the current policies of the rooms
* Have wellbeing information
* There should be a grid or table to display availability of rooms on specific days
* Administrators should be able to book rooms for students
* Once a room has been booked at a time, it cannot be booked by other students unless it is cancelled

/Non-Functional Requirements/

* The time from loading the initial login page completing a room booking should be very quick (1 minute max)
* The web page, database and servers should all be secure - meaning that they are not susceptible to malicious injection or information exposure.
* Ease of use should be a priority. No manual or instructions should be required by students in order to operate the application. Administrators have extra features so may need minimal coaching but the application should be intuitive enough for most to understand without.
* Accessible and fair to all users, rooms shouldn't be booked up by the same people constantly
* Efficient storage usage so that server costs are kept down
* The application and code should be easy to improve, extend and add features to. Code should be clean and well documented.
* The user interface should be aesthetically pleasing, to provide a positive experience so that users will want to use the app
* If the application is a success, other departments in the University may want the application to be extended to include them. The application should be able to scale up to accommodate this.
* The application should have a web user interface, and a server back-end
* Access to the application should be 24/7, with some allowance for updated and maintenance to the system itself
* Users should be able to access the application remotely, whether on the University network (Eduroam) or not.

/Flow Breakdown of Student Booking then Using a Room/

Basic Flow¹ -
*Student opens browser on phone/tablet/computer
*Student enters the URL for the Study Space Booking Application
*The student then clicks log in
*The student then logs in via single sign on
*At the home screen, the user chooses to click the "View rooms/Make a booking" button
*After picking the day they want to book, the user is presented with a grid/table displaying the availability of all the rooms that day
*The student may click a room to find out some more information
*After deciding on a room and an available slot, the user clicks it
*A confirmation page lets the user know that they have booked the room
*The student then goes to the room


Alternative Flow² -
*The student does not have access to the a personal device
*The student approaches a librarian/admin and asks them to book a room for them
*The admin logs in to their admin account, and repeats the process as above but books for the student

Exceptional Flow³ -
*A student wishes to book a slot that another student has already booked
*By the requirements, this is not allowed and so the student must find another, not already booked slot
*A student accidentally booked the wrong room
*The student can cancel the room by viewing their bookings, and clicking cancel

/Flow Breakdown of Admin Viewing Statistics/

Basic Flow¹ -
*Admin opens browser on phone/tablet/computer
*Admin enters the URL for the Study Space Booking Application
*The admin then clicks log in
*The admin then logs in via single sign on
*At the admin home screen, the admin clicks the "View statistics" button
*The admin is then presented with a set of options to view the statistics
*After selecting the options they want, the relevant statistics are presented

Exceptional Flow³ -
*The admin accidentally presses "View bookings" instead of "View statistics"
*They can just press the home button to go back to the home page and then press "View statistics" as intended

/Key/

* ¹ - Basic flow is the typical use of a user to perform the task
* ² - Alternative flow, a route that is different to the basic flow, but still reaches the same result
* ³ - Exceptional flows are where the user takes a route through the application that is not intended.

See attached use-case diagrams (x3).

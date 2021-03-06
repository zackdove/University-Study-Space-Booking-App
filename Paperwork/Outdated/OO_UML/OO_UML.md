//OO Design and UML//

/System Architecture/

See attached image. The system diagram shown gives a basic representation of how the system will be built whilst leaving out more complex parts of the system such as Single-Sign-On and API's. In essence the user will connect to a web server via their browser which communicates to an app server which is connected to our Oracle database. The basic system has the following external dependencies:

- API to link frontend and backend (likely to be developed in house)
- Single-Sign-On System
- Eventual integration to Bristol University server systems.

The web server component transfers information between the client side and the rest of the system. The app server deals with all the functionality of the system, whilst the database will hold all of the system's records and information.

We have decided to model the user booking mechanics in UML. This is because this is the most crucial part of the system, and we knew the least about how we were going to go about implementing it. We decided to do both active and static UML diagrams, and we learnt a lot from it, especially  the class diagram and collaboration diagram from which we now have a good understanding of the system structure and flow. For all of the following explanations, see the attached images for context.

/Collaboration Diagram/

The collaboration diagram aims to show how objects interact with each other through methods, and to define some sort of system flow through a critical part of the system. Here we are modelling the flow of a user object being created, and being used to create booking objects that then deal with the actual process of managing bookings in the SQL database via several methods.

There are three potential paths that could be taken once the user object has been created; make a new booking, edit an existing booking or delete a booking:

Make a new booking:

1. user.create_booking() is called to create a new booking object. This is filled with the user defined parameters, sourced from an input form on the front end. These input parameters become the attributes of the booking object.
2. booking.make_booking() is called, with its input parameters being the attributes of the object. This method queries the database to add a new booking entry into it.
3. booking.send_email() is called which sends a confirmation email to the user's email address.
4. The booking object nullifies itself.


Edit a Booking:

1. user.edit_booking() is called to create a new booking object. The input parameters are given by a user selection. These input parameters become the attributes of the booking object.
2. booking.edit_booking() is called, with its input parameters being the attributes of the object. This method queries the database to update an entry.
3. booking.send_email() is called which sends a confirmation email to the user's email address.
4. The booking object nullifies itself.

Delete a booking:

1. user.delete_booking() is called, with input given by the user. This queries the database and deletes the corresponding entry.

/Activity Diagram/

The activity diagram first starts with the user logging into the system through the imported UoB ‘Single Sign On’ integration. From here, the user is taken to the main menu where they can then choose whether they would like to make a new booking or view their existing bookings. 

Make a booking:

Once this option is selected, the system accesses the database and fetches all the bookings from the database (from all userIDs) and displays it in a timetable type format. The user can then select the specific timetable slot for which he would like to place a booking in. If it is free, the user will then be taken to a page to confirm his booking. Once the user has confirmed the booking, the system will instantiate a new booking object and set all the attributes appropriately. This booking object is then used to insert a new record into the Booking table within the database. An email is then sent once the booking has been saved, and the user is redirected to the main menu.

Viewing a booking:

Once this option is selected, the system accesses the database and fetches all the bookings of the user’s unique userID from the database. The constructor from the booking class is called (for each booking the user has) and a new booking is instantiated with the details from that specific record in the database. Each object is then displayed within a table (with their associated rooms shown), of which the user can select a booking they would like to delete. If the user selects to delete a booking, the booking.deleteBooking method is called and the system once again accesses the database and deletes the related record from the Booking table. An email is then sent to confirm the deletion, and the user is redirected to the main page.

View room functionality:

This view room function is used twice in the system (as shown in the flowchart). It is called when: 1) The user wants to make a new booking, and the timetable for each room is shown. 2) The user wants to view their bookings, and a list of the bookings and associated rooms are shown. This view room function works by fetching the specific room information from the Rooms table within the database. This fetched data is then formatted onto a webpage and shown to the user. 

/Class Diagram/

The object-class diagram is a type of static UML diagram used to display the structure of our system by outlining the classes, their methods and attributes and the relationships between each of the classes.

db class - This is our database class and is used to store the current instance of our database as well as the methods which allow us to query the database. The classes ‘user’ and ‘booking’ inherit from this class.
user class - This is the user class and is used to store all the data relating to the currently logged in user.

admin / student class – Each logged in user will either be a student or an admin, so two classes have been setup that inherit from the user class which classifies the current user as a student/admin.

booking class – The booking class inherits from ‘db’ and has a composition aggregation relationship with ‘student’ and ‘admin’. This implies a relationship where the child class (booking) cannot exist without the parent class existing (admin / student). If the admin / student class were to be deleted, the booking class would cease to exist. 

/Use-Case Diagram/

The Use Case Diagram is a dynamic UML model aimed to show the relationships between actors and use cases within a system. In this case the primary actors; those being Student and Admin, can call on the system for each of their use case, with secondary actors; being Single Sign-On or the Server responding to the request and the system being the Study Space Booking App. The centre System rectangle contains only use cases, where Log in, Make a booking, View room information, View booking, Edit a booking and Delete a booking are all use cases from the primary actors. Verify log in and Send confirmation email are secondary use cases to react to the primary use cases used for the secondary actors.

The Log in use case is the first use case called from either of the primary actors as it will be required to perform any of the other use cases. Verify log in is included as it’s a necessary use case for Single Sign-On to perform Log in.

Make a booking and View bookings don't follow an order and either can be called from either primary actors. View room information is extended from Make a booking so room information can be checked before booking. Edit a booking is extended from View bookings, Delete a booking is extended from Edit a booking. So to Edit a booking, a primary actor has to View bookings then Edit a booking. To Delete a booking, a primary actor has to View bookings, then Edit a booking and then Delete a booking. Send confirmation email is a use case for Server which is used every time a request to Make a booking, Edit a booking or Delete a booking is made.

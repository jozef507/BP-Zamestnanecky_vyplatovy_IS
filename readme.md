INFORMATION SYSTEM FOR EMPLOYEES AND SALARIES RECORDS
Bachelor thesis

This thesis aimed to design and create an information system for employees and salary records. The system is based on a client-server architecture and uses multiple technologies. First, the MySQL system was chosen for the database implementation, PHP language and CodeIgniter framework for the REST server interface, and lastly Java for both the desktop and also Android mobile client application. The implemented system offers the ability to calculate employees’ salaries, record their attendance, manage their accounts along with their personal and work data, and manage the legislative and company data as well.

The information system provides Android mobile client application, where company's employee can easily record his working performance. System supports all well-known types of working forms of wages - temporal, performance, proportional, so mobile app asks corresponidng data emloyee has to enter about his working performance which is agreed in the employment contract with the employer. These informations with empleyee's personal informations can be viewed by the employee/client in the app as well in profile section of the app.

The second client application of the system is desktop JavaFX application for employer. This is the thick-client aplication so calculations are performed there (not on server part of system). In the app, employer can create/update new employee, his login account, employment contract with important salary conditions, calculate salaries and edit important legislative conditions about salaries and emploment. 

The server part of the system is PHP/CodeIgniter REST-API designed for authorization, authentication and filling the database with data by the client applications. The server is connected to MySQL database server.

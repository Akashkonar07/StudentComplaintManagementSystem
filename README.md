**Student Complaint Management System (SCMS)**
This is a Student Complaint Management System (SCMS) developed using Java Swing for the frontend and PostgreSQL for the backend. The system helps students file complaints related to various categories and allows administrators to manage and resolve these complaints based on their roles.

**Features**
Student Login & Registration: Students can register and log in to file complaints and view the status of their existing complaints.
Admin Login & Dashboard: Admins (Class In-Charge, HOD, Council In-Charge, etc.) can log in and view complaints assigned to them.
Complaint Categories: Complaints can be filed under various categories like Class Concern, Academic Issues, Council Issues, Infrastructure Issues, etc.
Complaint Status Tracking: Students can track the status of their complaints (Pending, Resolved, In Progress).
Role-Based Access: Different roles (Class In-Charge, HOD, Principal) can access and resolve complaints relevant to their responsibilities.

**Tech Stack**
Frontend: Java Swing
Backend: PostgreSQL
Database: PostgreSQL

**Database Schema**
The database consists of the following tables:
Department: Stores department information (e.g., IT, CE).
Role: Stores roles like Student, Class In-Charge, HOD, etc.
Admin: Stores admin user details (email, password, role, department).
Student: Stores student details (name, email, department).
Complaint: Stores complaint details (complaint category, status, assigned role).

**Prerequisites**
Java 8 or higher
PostgreSQL
PostgreSQL JDBC Driver


**Steps to Set Up**
Clone the repository:

Set up PostgreSQL:
Create a new database in PostgreSQL.
Import the database schema provided in the sql/ directory (or manually create the tables and relationships).
Configure the database connection:

Open the DatabaseConnection class and update the connection details (username, password, and database URL) to match your PostgreSQL setup.
Build and run the project:

Open the project in your favorite IDE (e.g., IntelliJ IDEA, Eclipse).
Build the project and run the StudentComplaintManagementSystem class.

**Usage**
Student: After logging in, students can file complaints and view the status of their complaints.
Admin: Admins can log in based on their assigned role and view the complaints they are responsible for resolving.

**Screenshots of student interface**

![main](https://github.com/user-attachments/assets/e89fa240-f488-44a7-95d6-0e76b2e1a60c)
![file complaint](https://github.com/user-attachments/assets/c5a01851-4fb8-4851-8e53-801adc7cf39f)
![student dashboard](https://github.com/user-attachments/assets/c69a67b3-1bb0-43c7-9dfb-e55296ef84bd)


**Contributing**

*Fork the repository.

*Create a new branch (git checkout -b feature-name).

*Make your changes.

*Commit your changes (git commit -am 'Add feature').

*Push to the branch (git push origin feature-name)..

*Open a pull request.

**License**
This project is licensed under the MIT License - see the LICENSE.md file for details.



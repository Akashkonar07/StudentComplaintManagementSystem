CREATE TABLE department (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);
DELETE FROM department; -- Deletes all rows (use with caution!)

INSERT INTO department (id, name) VALUES (2, 'IT');
INSERT INTO department (id, name) VALUES (1, 'CE');

-- Create the Role table
CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL
);

-- Insert data into the Role table
INSERT INTO Role (role_name) VALUES
('Student'),
('Class In-Charge'),
('HOD'),
('Council In-Charge'),
('Ragging Cell'),
('Administration'),
('Principal');

CREATE TABLE admin (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role_id INT REFERENCES role(id),
    department_id INT REFERENCES department(id)
);
ALTER TABLE admin ADD COLUMN name VARCHAR(100);
ALTER TABLE admin ADD COLUMN dept_id INT; -- Adjust data type based on your needs

--Inserting admins data into admin table
INSERT INTO admin (email, password, role_id, department_id, name)
VALUES
    ('it.class@gst.in', 'it.classpass3', 2, 2, 'Kalpana Bodke'),
	('it.hod@gst.in', 'it.hodpass3', 3, 2, 'Seema Redekar'),
	('ce.class@gst.in', 'ce.classpass3', 2, 1, 'Samundiswary Srinivasan'),
	('ce.hod@gst.in', 'ce.hodpass3', 3, 1, 'Mahesh Biradar'),
    ('council.incharge@gst.in', 'councilpass4', 4, NULL, 'Smitha Kumar '),
	('ragging.cell@gst.in', 'raggingpass5', 5, NULL, 'Pratibha Sharma'),
	('office@gst.in', 'officepass6', 6, NULL, 'Vilas'),
    ('principal@gst.in', 'principalpass7', 7, NULL, 'Lakshmi Sudha');


DROP TABLE IF EXISTS student; -- Remove this line if you want to keep existing data
CREATE TABLE student (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100) -- Ensure this column is included
);
ALTER TABLE student ADD COLUMN password VARCHAR(100);
ALTER TABLE student ADD COLUMN dept_id INTEGER;
ALTER TABLE student
ADD CONSTRAINT fk_dept_id FOREIGN KEY (dept_id) REFERENCES department(id);

--Inserting students data into student table
INSERT INTO student (name, email, password, dept_id)
VALUES
    ('Akash', 'akash.it@edu.in', 'akash123', 2),
    ('Yuvraj', 'yuvraj.ce@edu.in', 'yuvraj123', 1),
    ('Sneha', 'sneha.it@edu.in', 'sneha123', 2),
    ('Amal', 'amal.ce@edu.in', 'amal123', 1),
    ('Bhoomi', 'bhoomi.ce@edu.in', 'bhoomi123', 2),
	('Vijay', 'vijay.it@edu.in', 'vijay123', 1);


CREATE TABLE complaint (
    id SERIAL PRIMARY KEY,
    student_id INT REFERENCES student(id),
    complaint_category VARCHAR(100),
    complaint_text TEXT,
    status VARCHAR(50),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE complaint ADD COLUMN category VARCHAR(255); -- Adjust data type as necessary
ALTER TABLE complaint ADD COLUMN assigned_role VARCHAR(255); -- Adjust data type as necessary

show port
select* from student;
select* from admin;
select* from complaint;
SELECT * FROM department;
select* from role;
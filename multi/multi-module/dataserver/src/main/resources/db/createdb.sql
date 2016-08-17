create database `orgdb`;
use `orgdb`;

CREATE TABLE `departments` (
  `department_id` int(8) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`department_id`)
);

CREATE TABLE `employees` (
  `employee_id` int(8) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(30) NOT NULL,
  `secondname` varchar(30) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `dob` date NOT NULL,
  `salary` double NOT NULL,
  `department_id` int(8) NOT NULL,
  PRIMARY KEY (`employee_id`),
  KEY `department_fk` (`department_id`),
  CONSTRAINT `department_fk` FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`)
);

CREATE INDEX  `department_id_key` on  `departments` (`department_id`);
CREATE INDEX  `employee_id_key` on  `employees` (`employee_id`);

use orgdb;


INSERT INTO `departments` ( `department_id` , `title`)
VALUES
(1,'Department X'), 
(2,'Department Y'),
(3,'Department Z'),
(4,'Empty Department');

INSERT INTO `employees` ( `employee_id` , `firstname`, `secondname`,  `surname`,`dob`,`salary`, `department_id`)
VALUES
(1, 'Elizabeth', 'Woolridge', 'Grant', '1999-02-12', 123.23, 1), 
(2, 'Eva', 'Henrietta', 'Silverstein', '1979-02-12', 2123.23, 1), 
(3, 'David', 'S.', 'Greathouse', '1955-02-02', 123.3, 3), 
(4, 'Ivan', 'Petrovich', 'Koutikov', '1974-02-11', 813, 2), 
(5, 'Desmond', 'Arthur', 'Harrington', '1939-06-12', 223.1, 1),
(6, 'Russell', 'J.', 'Dyer', '1976-06-06', 1003.0, 2),
(7, 'Michael', 'R.', 'Douglas', '1982-12-01', 500.0, 1),
(8, 'Alfred', 'P.', 'Sloan', '1952-09-11', 1500.0, 2),
(9, 'Asoke', 'Nath', 'Mitra', '1965-01-11', 320.0, 2),
(10, 'Ashoke', 'Kumar', 'Sen', '1955-12-31', 780.0, 2),
(11, 'Barton', 'N.', 'Zwiebach', '1945-12-12', 130.0, 2),
(12, 'Graham', 'G.', 'Ross', '1975-01-01', 20.0, 2),
(13, 'Lisa', 'Ana', 'Randall', '1978-11-11', 1720.0, 2),
(14, 'Cumrun', 'S.', 'Vafa', '1965-11-11', 980.0, 2),
(15, 'Nima', 'S.', 'Arkani-Hamed', '1985-03-23', 330.0, 2),
(16, 'Dimitros', 'A.', 'Dimopoulos', '1955-11-11', 666.0, 2),
(17, 'Albert', 'A.', 'Gore', '1943-10-05', 710.0, 2);


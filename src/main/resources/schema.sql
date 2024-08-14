CREATE TABLE IF NOT EXISTS Author (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(100) NOT NULL,
                        email VARCHAR(100) UNIQUE NOT NULL,
                        birthdate DATE
);

CREATE TABLE IF NOT EXISTS Course (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(100) NOT NULL,
                        description TEXT,
                        credit INT NOT NULL,
                        author_id INT,
                        FOREIGN KEY (author_id) REFERENCES Author(id)
);

CREATE TABLE IF NOT EXISTS Rating (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        number INT NOT NULL,
                        course_id INT,
                        FOREIGN KEY (course_id) REFERENCES Course(id)
);

CREATE TABLE IF NOT EXISTS Assessment (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            content TEXT,
                            course_id INT,
                            FOREIGN KEY (course_id) REFERENCES Course(id)
);
INSERT INTO Author (id, name, email) VALUES (1, 'John Doe', 'johndoe@sumerge.com');
-- INSERT INTO Course(name,description,credit,author_id) VALUES ('TestCourse2','testing',2,1)

DROP TABLE IF EXISTS ODONTOLOGO;

CREATE TABLE ODONTOLOGO (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    MATRICULA INT(50) NOT NULL,
    NOMBRE VARCHAR(100) NOT NULL,
    APELLIDO VARCHAR(100) NOT NULL
);

INSERT INTO ODONTOLOGO (ID,MATRICULA, NOMBRE, APELLIDO)
VALUES
    (DEFAULT,1050, 'Mario', 'Perez'),
    (DEFAULT,2231, 'Alex', 'Bravo');
;
DROP TABLE IF EXISTS Ownership;
DROP TABLE IF EXISTS Sales;
DROP TABLE IF EXISTS Company;

-- Firmy

CREATE TABLE Company (
  name VARCHAR(14) PRIMARY KEY
);

-- Transakcje miedzy firmami

CREATE TABLE Sales(
  id NUMERIC PRIMARY KEY,
  seller VARCHAR(14) NOT NULL REFERENCES Company,
  buyer VARCHAR(14) NOT NULL REFERENCES Company,
  price NUMERIC NOT NULL
);

-- Udzialy w firmach

CREATE TABLE Ownership(
  company VARCHAR(14) NOT NULL REFERENCES Company,
  shareholder VARCHAR(14) NOT NULL REFERENCES Company,
  fraction NUMERIC NOT NULL, 
  PRIMARY KEY (company, shareholder)
);


INSERT INTO Company VALUES ('Aussie Abalone');
INSERT INTO Company VALUES ('Big Bank');
INSERT INTO Company VALUES ('Candy Corp');
INSERT INTO Company VALUES ('Dubious Dairy');
INSERT INTO Company VALUES ('Eerie Eel');
INSERT INTO Company VALUES ('Fluffy Fudge');
INSERT INTO Company VALUES ('Golden Grate');
INSERT INTO Company VALUES ('Hungry Holding');
INSERT INTO Company VALUES ('Iffy Icecream');
INSERT INTO Company VALUES ('Jolly Jelly');
-- INSERT INTO Company VALUES ('Krafty Keg');
-- INSERT INTO Company VALUES ('Lovely Lolly');
-- INSERT INTO Company VALUES ('Mighty Mart');
-- INSERT INTO Company VALUES ('Nibbly Nuts');
-- INSERT INTO Company VALUES ('Oysters Only');
-- INSERT INTO Company VALUES ('Prairie Pies');



INSERT INTO Sales VALUES (10,'Aussie Abalone','Big Bank',1000);
INSERT INTO Sales VALUES (11,'Aussie Abalone','Big Bank',1000);
INSERT INTO Sales VALUES (12,'Aussie Abalone','Big Bank',1000);
INSERT INTO Sales VALUES (13,'Candy Corp','Big Bank',100);

INSERT INTO Sales VALUES (20,'Candy Corp','Aussie Abalone',100);

INSERT INTO Sales VALUES (30,'Aussie Abalone','Hungry Holding',200);
INSERT INTO Sales VALUES (31,'Candy Corp','Hungry Holding',100);
INSERT INTO Sales VALUES (32,'Candy Corp','Hungry Holding',100);
INSERT INTO Sales VALUES (33,'Aussie Abalone','Hungry Holding',100);

INSERT INTO Sales VALUES (40,'Dubious Dairy','Fluffy Fudge',1000);

INSERT INTO Sales VALUES (50,'Fluffy Fudge','Candy Corp',1600);
INSERT INTO Sales VALUES (51,'Iffy Icecream','Candy Corp',800);
INSERT INTO Sales VALUES (52,'Jolly Jelly','Candy Corp',400);
INSERT INTO Sales VALUES (53,'Jolly Jelly','Candy Corp',200);
INSERT INTO Sales VALUES (54,'Aussie Abalone','Candy Corp',100);

INSERT INTO Sales VALUES (60,'Fluffy Fudge','Eerie Eel',100);
INSERT INTO Sales VALUES (61,'Dubious Dairy','Eerie Eel',500);
INSERT INTO Sales VALUES (62,'Big Bank','Eerie Eel',1000);
INSERT INTO Sales VALUES (63,'Hungry Holding','Eerie Eel',10000);
INSERT INTO Sales VALUES (64,'Iffy Icecream','Eerie Eel',1000);


INSERT INTO Ownership VALUES ('Dubious Dairy','Big Bank',0.30);
INSERT INTO Ownership VALUES ('Eerie Eel','Big Bank',0.30);

INSERT INTO Ownership VALUES ('Fluffy Fudge','Dubious Dairy',0.10);
INSERT INTO Ownership VALUES ('Iffy Icecream','Dubious Dairy',0.50);

INSERT INTO Ownership VALUES ('Fluffy Fudge','Eerie Eel', 0.01);

INSERT INTO Ownership VALUES ('Aussie Abalone','Hungry Holding',0.50);

INSERT INTO Ownership VALUES ('Dubious Dairy','Golden Grate',0.50);

INSERT INTO Ownership VALUES ('Jolly Jelly','Candy Corp',0.50);
INSERT INTO Ownership VALUES ('Fluffy Fudge','Candy Corp',0.50);
INSERT INTO Ownership VALUES ('Iffy Icecream','Candy Corp',0.50);


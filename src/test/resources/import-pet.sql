-- Mascotas
INSERT INTO pet (id, name, address, about, age, breed_id)
VALUES (1, 'Buddy', '123 Main St', 'Friendly dog', '2023-06-01', 1);

INSERT INTO pet (id, name, address, about, age, breed_id)
VALUES (2, 'Daisy', '123 Main St', 'Friendly dog', '2022-06-21', 2);

ALTER SEQUENCE pet_seq RESTART WITH 2;
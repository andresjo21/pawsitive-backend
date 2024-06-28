-- Razas de perros
INSERT INTO breed (id, name, animal_type_id) VALUES (1, 'Labrador', 1); -- Labrador es una raza de perro
INSERT INTO breed (id, name, animal_type_id) VALUES (2, 'Poodle', 1); -- Poodle es una raza de perro
INSERT INTO breed (id, name, animal_type_id) VALUES (3, 'Dóberman', 1); -- Dóberman es una raza de perro
INSERT INTO breed (id, name, animal_type_id) VALUES (4, 'Bulldog Francés', 1); -- Bulldog Francés es una raza de perro
INSERT INTO breed (id, name, animal_type_id) VALUES (5, 'Boxer', 1); -- Boxer es una raza de perro
INSERT INTO breed (id, name, animal_type_id) VALUES (6, 'Dálmata', 1); -- Dálmata es una raza de perro
INSERT INTO breed (id, name, animal_type_id) VALUES (35, 'Salchicha', 1);

-- Razas de caballos
INSERT INTO breed (id, name, animal_type_id) VALUES (7, 'Pura Sangre', 2); -- Pura Sangre es una raza de caballo
INSERT INTO breed (id, name, animal_type_id) VALUES (8, 'Cuarto de Milla', 2); -- Cuarto de Milla es una raza de caballo
INSERT INTO breed (id, name, animal_type_id) VALUES (9, 'Árabe', 2); -- Árabe es una raza de caballo
INSERT INTO breed (id, name, animal_type_id) VALUES (10, 'Appaloosa', 2); -- Appaloosa es una raza de caballo
INSERT INTO breed (id, name, animal_type_id) VALUES (11, 'Frisón', 2); -- Frisón es una raza de caballo
INSERT INTO breed (id, name, animal_type_id) VALUES (12, 'Andaluz', 2); -- Andaluz es una raza de caballo

-- Razas de monos
INSERT INTO breed (id, name, animal_type_id) VALUES (13, 'Capuchino', 4); -- Capuchino es una raza de mono
INSERT INTO breed (id, name, animal_type_id) VALUES (14, 'Tamarino', 4); -- Tamarino es una raza de mono


-- Razas de gatos en inglés
INSERT INTO breed (id, name, animal_type_id) VALUES (15, 'Siamese', 3); -- Siamese es una raza de gato
INSERT INTO breed (id, name, animal_type_id) VALUES (16, 'Persian', 3); -- Persian es una raza de gato
INSERT INTO breed (id, name, animal_type_id) VALUES (17, 'Maine Coon', 3); -- Maine Coon es una raza de gato
INSERT INTO breed (id, name, animal_type_id) VALUES (18, 'British Shorthair', 3); -- British Shorthair es una raza de gato

-- Tipos de hámster en inglés
INSERT INTO breed (id, name, animal_type_id) VALUES (19, 'Syrian Hamster', 5); -- Syrian Hamster es un tipo de hámster
INSERT INTO breed (id, name, animal_type_id) VALUES (20, 'Roborovski Hamster', 5); -- Roborovski Hamster es un tipo de hámster
INSERT INTO breed (id, name, animal_type_id) VALUES (21, 'Dwarf Campbell Russian Hamster', 5); -- Dwarf Campbell Russian Hamster es un tipo de hámster
INSERT INTO breed (id, name, animal_type_id) VALUES (22, 'Chinese Hamster', 5); -- Chinese Hamster es un tipo de hámster

INSERT INTO breed (id, name, animal_type_id) VALUES (23, 'Holland Lop', 6); -- Holland Lop es una raza de conejo
INSERT INTO breed (id, name, animal_type_id) VALUES (24, 'Mini Rex', 6); -- Mini Rex es una raza de conejo

INSERT INTO breed (id, name, animal_type_id) VALUES (25, 'Betta Fish', 7); -- Betta Fish es una raza de pez
INSERT INTO breed (id, name, animal_type_id) VALUES (26, 'Goldfish', 7); -- Goldfish es una raza de pez

INSERT INTO breed (id, name, animal_type_id) VALUES (27, 'Holstein Cow', 8); -- Holstein Cow es una raza de vaca
INSERT INTO breed (id, name, animal_type_id) VALUES (28, 'Jersey Cow', 8); -- Jersey Cow es una raza de vaca

INSERT INTO breed (id, name, animal_type_id) VALUES (29, 'Nubian Goat', 9); -- Nubian Goat es una raza de cabra
INSERT INTO breed (id, name, animal_type_id) VALUES (30, 'Saanen Goat', 9); -- Saanen Goat es una raza de cabra

INSERT INTO breed (id, name, animal_type_id) VALUES (31, 'Parakeet', 10); -- Parakeet es una raza de ave
INSERT INTO breed (id, name, animal_type_id) VALUES (32, 'Canary', 10); -- Canary es una raza de ave

INSERT INTO breed (id, name, animal_type_id) VALUES (33, 'Abyssinian Guinea Pig', 11); -- Abyssinian Guinea Pig es una raza de cuy
INSERT INTO breed (id, name, animal_type_id) VALUES (34, 'American Guinea Pig', 11); -- American Guinea Pig es una raza de cuy

ALTER SEQUENCE breed_seq RESTART WITH 35;
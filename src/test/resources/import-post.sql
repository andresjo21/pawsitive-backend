-- Publicaciones
INSERT INTO post (id, show, post_type_id, user_id, pet_id)
VALUES (1, true, 1, 1, 1);

INSERT INTO post (id, show, post_type_id, user_id, pet_id)
VALUES (2, true, 1, 1, 2);

ALTER SEQUENCE post_seq RESTART WITH 3;
-- Imágenes
INSERT INTO image (id, file_name) VALUES (1, 'image1.jpg');
INSERT INTO image (id, file_name) VALUES (2, 'image2.jpg');
ALTER SEQUENCE image_seq RESTART WITH 3;
INSERT INTO post_image (image_id, post_id) VALUES (1, 1);
INSERT INTO user_image (image_id, user_id) VALUES (2, 1);
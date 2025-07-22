drop database if exists petnote_test;
create database petnote_test;
use petnote_test;

CREATE TABLE `user` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `email` varchar(250) UNIQUE NOT NULL,
  `password` varchar(50) NOT NULL,
  `profilePictureURL` text,
  `isVerified` boolean DEFAULT false,
  `createdAt` timestamp default current_timestamp
);

CREATE TABLE `pet` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `profilePictureURL` text,
  `petName` varchar(100) NOT NULL,
  `breed` varchar(100),
  `species` varchar(100),
  `age` int,
  `weight` float,
  `userId` int,
  `createdAt` timestamp default current_timestamp
);

CREATE TABLE `activity` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `activityDate` datetime NOT NULL,
  `activityName` varchar(100),
  `petId` int NOT NULL,
  `userId` int NOT NULL,
  `completed` boolean
);

CREATE TABLE `note` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `createdAt` datetime NOT NULL default current_timestamp,
  `editedAt` datetime default current_timestamp,
  `description` varchar(500),
  `pet_id` int NOT NULL
);

ALTER TABLE `note` ADD FOREIGN KEY (`pet_id`) REFERENCES `pet` (`id`) ON DELETE CASCADE;

ALTER TABLE `activity` ADD FOREIGN KEY (`petId`) REFERENCES `pet` (`id`) on delete cascade;

ALTER TABLE `activity` ADD FOREIGN KEY (`userId`) REFERENCES `user` (`id`) on delete cascade;

ALTER TABLE `pet` ADD FOREIGN KEY (`userId`) REFERENCES `user` (`id`) on delete cascade;

DELIMITER //

CREATE PROCEDURE set_known_good_state()
BEGIN

  -- Delete in child-to-parent order to satisfy FK constraints
  DELETE FROM activity;
  DELETE FROM note;
  DELETE FROM pet;
  DELETE FROM `user`;

  -- Reset AUTO_INCREMENT
  ALTER TABLE `user` AUTO_INCREMENT = 1;
  ALTER TABLE pet AUTO_INCREMENT = 1;
  ALTER TABLE note AUTO_INCREMENT = 1;
  ALTER TABLE activity AUTO_INCREMENT = 1;

  -- Seed users
  INSERT INTO `user` (id, username, email, `password`, profilePictureURL) VALUES
  (1, 'user1', 'email1@email.com', 'password123', 'https://placekitten.com/200/200'),
  (2, 'user2', 'email2@email.com','password123', 'https://placekitten.com/200/200'),
  (3, 'user3', 'email3@email.com', 'password123', 'https://placekitten.com/200/200');

  -- Seed pets
  INSERT INTO pet (id, petName, species, userId, profilePictureURL) VALUES
  (1, 'Buddy', 'Dog', 1, 'https://placekitten.com/150/150'),
  (2, 'Mittens', 'Cat', 2, 'https://placekitten.com/150/150'),
  (3, 'Rex', 'Dog', 1, 'https://placekitten.com/150/150'),
  (4, 'Fluffy', 'Cat', 3, 'https://placekitten.com/150/150'),
  (5, 'Whiskers', 'Cat', 2, 'https://placekitten.com/150/150');

  -- Seed notes
  INSERT INTO note (id, pet_id, `description`) VALUES
  (1, 1, 'Fed the pet this morning.'),
  (2, 1, 'Went on a walk.'),
  (3, 2, 'Groomed today.'),
  (4, 3, 'Visited the vet for a checkup.'),
  (5, 4, 'Played fetch for 20 minutes.'),
  (6, 5, 'Took a nap on the couch.'),
  (7, 2, 'Scratched the sofa again...'),
  (8, 3, 'Very energetic today.'),
  (9, 4, 'Tried a new food.'),
  (10, 1, 'Training session complete.');

  -- Seed activities (with activityDate and userId)
  INSERT INTO activity (id, petId, userId, activityDate, `activityName`, completed) VALUES
  (1, 1, 1, '2024-07-21 08:00:00', 'Morning Walk', true),
  (2, 1, 1, '2024-07-21 08:15:00', 'Feed Breakfast', true),
  (3, 1, 1, '2024-07-21 08:30:00', 'Brushing Fur', false),
  (4, 2, 2, '2024-07-21 09:00:00', 'Litter Box Cleaning', true),
  (5, 2, 2, '2024-07-21 09:30:00', 'Grooming', false),
  (6, 3, 1, '2024-07-21 10:00:00', 'Training Session', true),
  (7, 3, 1, '2024-07-21 10:30:00', 'Vet Visit', true),
  (8, 4, 3, '2024-07-21 11:00:00', 'Playtime', true),
  (9, 5, 2, '2024-07-21 11:30:00', 'Nail Clipping', false),
  (10, 5, 2, '2024-07-21 12:00:00', 'Treat Time', true);

END //

DELIMITER ;

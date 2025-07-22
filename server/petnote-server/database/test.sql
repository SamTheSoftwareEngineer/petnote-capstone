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
  `createdAt` timestamp DEFAULT (now())
);

CREATE TABLE `pet` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `profilePictureURL` text,
  `petName` varchar(100) NOT NULL,
  `breed` varchar(100),
  `age` int,
  `weight` float,
  `userId` int,
  `createdAt` timestamp DEFAULT (now())
);

CREATE TABLE `activity` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `activityDate` datetime NOT NULL,
  `activityName` varchar(100),
  `petId` int NOT NULL,
  `userId` int NOT NULL
);

CREATE TABLE `note` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `createdAt` datetime NOT NULL,
  `editedAt` datetime,
  `description` varchar(500),
  `pet_id` int NOT NULL
);

ALTER TABLE `note` ADD FOREIGN KEY (`id`) REFERENCES `pet` (`id`) on delete cascade;

ALTER TABLE `activity` ADD FOREIGN KEY (`petId`) REFERENCES `pet` (`id`) on delete cascade;

ALTER TABLE `activity` ADD FOREIGN KEY (`userId`) REFERENCES `user` (`id`) on delete cascade;

ALTER TABLE `pet` ADD FOREIGN KEY (`userId`) REFERENCES `user` (`id`) on delete cascade;

delimiter //

create procedure set_known_good_state()
begin 

-- Clean up existing data (order matters due to FK constraints)
DELETE FROM activities;
DELETE FROM notes;
DELETE FROM pets;
DELETE FROM users;

-- Seed users
INSERT INTO users (id, username, password, profile_picture_url) VALUES
(1, 'user1', 'password123', 'https://placekitten.com/200/200'),
(2, 'user2', 'password123', 'https://placekitten.com/200/200'),
(3, 'user3', 'password123', 'https://placekitten.com/200/200');

-- Seed pets
INSERT INTO pets (id, name, species, owner_id, profile_picture_url) VALUES
(1, 'Buddy', 'Dog', 1, 'https://placekitten.com/150/150'),
(2, 'Mittens', 'Cat', 2, 'https://placekitten.com/150/150'),
(3, 'Rex', 'Dog', 1, 'https://placekitten.com/150/150'),
(4, 'Fluffy', 'Cat', 3, 'https://placekitten.com/150/150'),
(5, 'Whiskers', 'Cat', 2, 'https://placekitten.com/150/150');

-- Seed notes
INSERT INTO notes (id, pet_id, content) VALUES
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

-- Seed activities
INSERT INTO activities (id, pet_id, name, completed) VALUES
(1, 1, 'Morning Walk', true),
(2, 1, 'Feed Breakfast', true),
(3, 1, 'Brushing Fur', false),
(4, 2, 'Litter Box Cleaning', true),
(5, 2, 'Grooming', false),
(6, 3, 'Training Session', true),
(7, 3, 'Vet Visit', true),
(8, 4, 'Playtime', true),
(9, 5, 'Nail Clipping', false),
(10, 5, 'Treat Time', true);

end //
delimiter ;




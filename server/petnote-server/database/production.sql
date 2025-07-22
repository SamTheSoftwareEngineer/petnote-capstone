drop database if exists petnote;
create database petnote;
use petnote;

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



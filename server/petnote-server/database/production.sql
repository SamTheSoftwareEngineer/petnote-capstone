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

ALTER TABLE `Note` ADD FOREIGN KEY (`id`) REFERENCES `Pet` (`id`) on delete cascade;

ALTER TABLE `Activity` ADD FOREIGN KEY (`petId`) REFERENCES `Pet` (`id`) on delete cascade;

ALTER TABLE `Activity` ADD FOREIGN KEY (`userId`) REFERENCES `User` (`id`) on delete cascade;

ALTER TABLE `Pet` ADD FOREIGN KEY (`userId`) REFERENCES `User` (`id`) on delete cascade;

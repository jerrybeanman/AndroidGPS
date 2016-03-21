<?php
    /*-----------------------------------------------------------------------------------------------
--    Name:     [DatabaseCreation]         Date:         [March 18th, 2016]
--
--    Designer: [Vivek Kalia]                Programmer:   [Vivek Kalia]
--
--    Interface:  N/A
--                
--
--
--    Notes: Creates the database with the structure that will be required for user 
--            authentication and location storage.
------------------------------------------------------------------------------------------------*/
   

    try
    {
	$db = new PDO('mysql:host=localhost;dbname=android_gps;', 'root', 'c0mmaudi0');
    } catch (PDOException $e) {
		echo 'failure!';
    } 

   $create_user_table = 
		"CREATE TABLE IF NOT EXISTS `users` (
		   `user_id`  INT(11)      NOT NULL AUTO_INCREMENT,
		   `username` VARCHAR(64)  NOT NULL,
		   `password` VARCHAR(64)  NOT NULL,
		    PRIMARY KEY (`user_id`),
		    UNIQUE KEY `username` (`username`) 
		)";

   $query = $db->prepare($create_user_table);
   $table = ( ($query->execute()) ? "Users table created" : "Users table error");
   echo $table;   
   $create_location_table =
		"CREATE TABLE IF NOT EXISTS `location` (
		   `location_id` INT(11)       NOT NULL AUTO_INCREMENT,
		   `user_id`     INT(11)       NOT NULL,
		   `rec_time`    DATETIME      NOT NULL,
		   `ip_address`  VARCHAR(100)  NOT NULL,
		   `dev_name`    VARCHAR(100)  NOT NULL,
		   `latitude`    VARCHAR(100)  NOT NULL,
		   `longitude`   VARCHAR(100)  NOT NULL,
		    PRIMARY KEY  (`location_id`),
		    FOREIGN KEY `user_id` REFERENCES users (`user_id`)
		)"; 

   $query2 = $db->prepare($create_location_table);
   $table2 = ( ($query->execute()) ? "Location table created" : "Location table error");
   echo $table2;

?>

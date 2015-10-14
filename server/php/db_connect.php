<?php
	
	//The path to the db-config file
	define('DB_CONFIG', __DIR__ . '/db_config.php');
	
	/**
		* A class to connect to to a MySQL-database.
		* The database information must be entered in the file given in 'DB_CONFIG'.
		* The following variables MUST be declared:
		* DB_USER - database user
		* DB_PASSWORD - matching database password for the user
		* DB_DATABASE - name of the database
		* DB_SERVER - serveradress of the database
	*/
	class DB_CONNECT {
		
		// constructor
		function __construct() {
			// connecting to database
			$this->connect();
		}
		
		// destructor
		function __destruct() {
			// closing db connection
			$this->close();
		}
		
		/**
			* Function to connect with the database defined in '/db_config.php';
		*/
		function connect() {
			// import database connection variables
			require_once DB_CONFIG;
			
			// Connecting to mysql database
			$con = mysql_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysql_error());
			
			// Selecing database
			$db = mysql_select_db(DB_DATABASE) or die(mysql_error()) or die(mysql_error());
			
			// returing connection cursor
			return $con;
		}
		
		/**
			* Function to close db connection
		*/
		function close() {
			// closing db connection
			mysql_close();
		}
		
	}
	
?>
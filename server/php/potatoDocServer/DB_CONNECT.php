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
            
            private $db;
		
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
			$this->db = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
                        
                        if($this->db ->connect_errno > 0) {
                            die('Unable to connect to database [' . $this->db->connect_error . ']');
                        }
			
			// returing connection cursor
			return $this->db;
		}
		
		/**
			* Function to close db connection
		*/
		function close() {
			// closing db connection
			$this->db->close();;
		}
		
	}
	
?>
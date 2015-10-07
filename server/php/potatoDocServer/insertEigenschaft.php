<?php
	
	//Path to the 'db_connection'-file
	define('DB_CONNECT_PATH', __DIR__ . '/db_connect.php');
	
	//JSON response
	$response = array();
	
	if( isset($_GET['eig_name']) ){
		
		$eig_name = $_GET['eig_name'];
		
		//Include needed db_connection class
		require_once DB_CONNECT_PATH;
		
		//Establish db connection
		$db = new DB_CONNECT();
		
		//Insert a row
		$query_result = mysql_query("INSERT INTO eigenschaft(eig_name) VALUES('$eig_name')");
		
		if($query_result){
			$response["success"] = 1;
			$response["message"] = "Eigenschaft wurde erfolgreich eingefuegt.";
			} else{
			$response["success"] = 0;
			$response["message"] = "Eigenschaft konnte nicht eingefuegt werden.";
		}
		
		echo json_encode($response);
		} else {
		$response["success"] = 0;
		$response["message"] = "Nicht alle noetigen Parameter uebergeben!";
		
		echo json_encode($response);
	}
	
?>
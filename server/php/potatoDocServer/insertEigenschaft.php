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
		$dbConnect = new DB_CONNECT();
            
                //Get the actual mysqli object
                $db = $dbConnect->getDB();
                
		//Prepare the statement, bind arguments and execute
		$statement = $db->prepare("INSERT INTO eigenschaft(eig_name) VALUES(?)");
                
                $statement->bind_param('s', $eig_name);
                
                $query_result = $statement->execute();
		
                //check success
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
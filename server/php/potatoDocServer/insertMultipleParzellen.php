<?php
	
	$response = array();
	
	if(!isset($_GET["par_per_row"])){
		$response['success'] = 0;
		$response['message'] = 'Nicht erfolgreich!';
		echo json_encode($response);
		exit();
	}
	
	//The path to the db-config file
	define('DB_FUNCTIONS', __DIR__ . '/DB_FUNCTIONS.php');
	
	// import database connection variables
	require_once DB_FUNCTIONS;
	// attributes
	//signal included files to not print their result
	$printDirect = 1;
	
	//get all variables
	$par_per_row = $_GET["par_per_row"];
	
	//Begin inserting
	$db->beginTransaction();
	
	$success = true;
	
	$targetRow = $_GET["parz_row"];
	
	for($nrOfRows = 0; $nrOfRows < $_GET["nr_of_rows"]; $nrOfRows++){
		//adjust parz_row
		$_GET["parz_row"] = $targetRow + $nrOfRows;
		
		for($i = 0; $i < $par_per_row; $i++){
			include 'insertParzelleIntoRow.php';
			$wasInserted = json_decode($jsonResult, true)['success'];
			if(!$wasInserted){
				$success = false;
				break;
			}
		}
		
		if(!$wasInserted){		
			break;
		}
	}
	
	if ($success) {
		$db->commit();
		$response['success'] = 1;
		$response['message'] = 'Erfolgreich eingefügt!';
		echo json_encode($response);
		} else {
		$db->rollBack();
		$response['success'] = 0;
		$response['message'] = 'Nicht erfolgreich!';
		echo json_encode($response);
	}
	

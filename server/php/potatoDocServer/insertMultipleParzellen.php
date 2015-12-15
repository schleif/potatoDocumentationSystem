<?php
	
	$response = array();
	
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
	
	//insert aufgabe
	for($i = 0; $i < $par_per_row; i++){
		include 'insertParzelleIntoRow.php';
		$wasInserted = json_decode($jsonResult, true)['success'];
		if(!$wasInserted){
			$success = false;
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
	

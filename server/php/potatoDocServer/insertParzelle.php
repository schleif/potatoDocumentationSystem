<?php

//Path to the 'db_connection'-file
define('DB_CONNECT_PATH', __DIR__ . '/db_connect.php');

//JSON response
$response = array();

// the values of the sql table
// table name
define('table_name', 'parzelle');

// attributes
// (name, typ)
$values = array(
    'atr1' => 'parz_id',
    'atr2' => 'feld_nr',
    'atr3' => 'sorte'
);

// this params will bind to the query
$bind_params = array();

$isset_param = true;
foreach ($values as $value) {
    // If all values set
    if (!isset($_GET[$value])) {
        $isset_param = false;
        break;
    }
    
    //filling bind_params
    array_push($bind_params, $_GET[$value]);
}

if ($isset_param) {

    //Include needed db_connection class
    require_once DB_CONNECT_PATH;

    //Establish db connection
    $dbConnect = new DB_CONNECT();

    //Get the actual mysqli object
    $db = $dbConnect->getDB();
    
    echo "Datenbank fertig";

    //Prepare the statement, bind arguments and execute
    $sql = "INSERT INTO " . table_name . "( ";
    $first = true;
    foreach ($values as $value) {
        //Appending value
        if ($first) {
        $sql .= $value;
            $first = false;
        } else {
           $sql .= ", " . $value; 
        }
    }
    $sql .= " ) VALUES ( ";
    $first = true;
    foreach ($values as $value) {
        if ($first) {
            $sql .= "?";
            $first = false;
        } else {
            $sql .= " , ?";
        }
    }
    $sql .= " ) ";
    
    echo $sql;
    
    try {
        $stmt = $db->prepare($sql);
        $query_result = $stmt->execute($bind_params);
    } catch (PDOException $ex) {
        trigger_error('Wrong SQL: ' . $sql . ' Error: ' . $ex->getMessage(), E_USER_ERROR);
    }
    
    echo "statement ausgeführt";

    //check success
    if ($query_result) {
        $response["success"] = 1;
        $response["message"] = "Eigenschaft wurde erfolgreich eingefuegt.";
    } else {
        $response["success"] = 0;
        $response["message"] = "Eigenschaft konnte nicht eingefuegt werden.";
    }

    echo json_encode($response);
} else {
    $response["success"] = 0;
    $response["message"] = "Nicht alle noetigen Parameter uebergeben!";

    echo json_encode($response);
}
	

<?php

//Path to the 'db_connection'-file
define('DB_CONNECT_PATH', __DIR__ . '/DB_CONNECT.php');

//Include needed db_connection class
require_once DB_CONNECT_PATH;


//Establish db connection
$dbConnect = new DB_CONNECT();

//Get the actual PDO object
$db = $dbConnect->getDB();

/**
 * Processing the sql Statement gives result Array back.
 * @param String $sql Statement
 * @return type 
 */
function connectPrepareResult($sql, $bindparams) {

    global $db;

    // Prepare and execute
    try {
        $stmt = $db->prepare($sql);
        if (isset($bindparams)) {
            $query_result = $stmt->execute($bindparams);
        } else {
            $query_result = $stmt->execute();
        }
    } catch (PDOException $ex) {
        echo 'Wrong SQL: ' . $sql . ' Error: ' . $ex->getMessage();
    }

    return array($stmt, $query_result);
}

function select($procedure) {
    // Initializing JSON response
    $response = array();

    //Defining Statement
    $sql = "Call $procedure ()";

    $res = connectPrepareResult($sql, null);

    // Processing result
    if ($res[1]) {
        //If the query was executed successfully
        $response["success"] = 1;
        $resultLine = array();

        // All Result rows will be pushed in $resultLine array
        while ($row = $res[0]->fetch(PDO::FETCH_ASSOC)) {
            array_push($resultLine, $row);
            // $newLine = "";
            //foreach($row as $value) {
            //  $newLine .= "$value;";
            //}
            // echo $newLine;
        }

        //Add all rows to the response
        $response["Result"] = $resultLine;
    } else {
        $response["success"] = 0;
    }

    print json_encode($response);
}

/**
 * Function inserts in specified table the specified values;
 * @param type $tableName
 * @param type $values
 */
function insert($tableName, $values) {
    //JSON response
    $response = array();

    // the values of the sql table
    // table name
    define('table_name', $tableName);

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
        //Prepare the statement, bind arguments and execute
        $sql = "INSERT INTO " . table_name . " ( ";
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

        $res = connectPrepareResult($sql, $bind_params);

        //check success
        if ($res[1]) {
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
}

/**
 * Calls a StoredProcedure with the name $procedure 
 * and checks if all needed GET parameters - given in the array 
 * $values - are set.
 */
function insertCall($procedure, $values) {
    //JSON response
    $response = array();

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
        //Prepare the statement, bind arguments and execute
        $sql = "CALL " . $procedure . " ( ";
        $first = true;
        foreach ($values as $value) {
            //Appending value
            if ($first) {
                $sql .= '?';
                $first = false;
            } else {
                $sql .= ", ?";
            }
        }
        $sql .= " ) ";

        $res = connectPrepareResult($sql, $bind_params);

        //check success
        if ($res[1]) {
            $response["success"] = 1;
            $response["message"] = "Eigenschaft wurde erfolgreich eingefuegt.";
        } else {
            $response["success"] = 0;
            $response["message"] = "Eigenschaft konnte nicht eingefuegt werden.";
        }

        return json_encode($response);
        
    } else {
        $response["success"] = 0;
        $response["message"] = "Nicht alle noetigen Parameter uebergeben!";

        return json_encode($response);
    }
}

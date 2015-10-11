<?php

function select($procedure) {
    //Path to the 'db_connection'-file
    define('DB_CONNECT_PATH', __DIR__ . '/DB_CONNECT.php');

    //JSON response
    $response = array();

    //Include needed db_connection class
    require_once DB_CONNECT_PATH;

    //Establish db connection
    $dbConnect = new DB_CONNECT();

    //Get the actual PDO object
    $db = $dbConnect->getDB();

    $sql = "Call $procedure ()";
    try {
        $stmt = $db->prepare($sql);
        $query_result = $stmt->execute();
    } catch (PDOException $ex) {
        echo 'Wrong SQL: ' . $sql . ' Error: ' . $ex->getMessage();
    }
    $first = true;
    if ($query_result) {
        while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $newLine = "";
            foreach($row as $value) {
                $newLine .= "$value;";
            }
            echo $newLine;
        }
    }
}

/**
 * Function inserts in specified table the specified values;
 * @param type $tableName
 * @param type $values
 */
function insert($tableName, $values) {
    //Path to the 'db_connection'-file
    define('DB_CONNECT_PATH', __DIR__ . '/DB_CONNECT.php');

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

        //Include needed db_connection class
        require_once DB_CONNECT_PATH;

        //Establish db connection
        $dbConnect = new DB_CONNECT();

        //Get the actual PDO object
        $db = $dbConnect->getDB();

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

        try {
            $stmt = $db->prepare($sql);
            $query_result = $stmt->execute($bind_params);
        } catch (PDOException $ex) {
            echo 'Wrong SQL: ' . $sql . ' Error: ' . $ex->getMessage();
        }

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
}

/**
 * Calls a StoredProcedure with the name $procedure 
 * and checks if all needed GET parameters - given in the array 
 * $values - are set.
 */
function insertCall($procedure, $values) {
    //Path to the 'db_connection'-file
    define('DB_CONNECT_PATH', __DIR__ . '/DB_CONNECT.php');

    //JSON response
    $response = array();

    // the values of the sql table
    // table name
    define('PROCEDURE_NAME', $procedure);

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

        //Prepare the statement, bind arguments and execute
        $sql = "CALL " . PROCEDURE_NAME . " ( ";
        $first = true;
        foreach ($values as $value) {
            //Appending value
            if ($first) {
                $sql .= '?';
                $first = false;
            } else {
                $sql .= ", " . $value;
            }
        }
        $sql .= " ) ";

        try {
            $stmt = $db->prepare($sql);
            $query_result = $stmt->execute($bind_params);
        } catch (PDOException $ex) {
            echo 'Wrong SQL: ' . $sql . ' Error: ' . $ex->getMessage();
        }

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
}

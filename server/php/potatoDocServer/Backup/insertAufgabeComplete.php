<?php

$response = array();

//abort if json is not parsed
if (!isset($_GET['json'])) {
    $response['success'] = 0;
    echo json_encode($response);
    return;
}

//The path to the db-config file
define('DB_FUNCTIONS', __DIR__ . '/DB_FUNCTIONS.php');

// import database connection variables
require_once DB_FUNCTIONS;
// attributes
//signal included files to not print their result
$printDirect = 1;

//get all variables
$jsonArray = json_decode($_GET['json'], true);

$aufg_name = $jsonArray['aufg_name'];

//Create array of all properties
$properties = $jsonArray['eigenschaften'];

//Create array of all dates
$dates = $jsonArray['dates'];

//Begin inserting
$db->beginTransaction();

//insert aufgabe
$_GET['aufg_name'] = $aufg_name;
include 'insertAufgabe.php';
$wasInserted = json_decode($jsonResult, true)['success'];
$success = $wasInserted;


//insert all properties
foreach ($properties as $value) {
    $_GET['eig_name'] = $value;
    include './insertAufg_beinhaltet_eig.php';
    $success = $success && json_decode($jsonResult, true)['success'];
}

//insert all dates
$length = count($dates);
for ($i = 0; $i < $length; $i++) {
    $_GET['fromDate'] = $dates[$i];
    $_GET['toDate'] = $dates[++$i];

    include './insertAufg_termin.php';
    $success = $success && json_decode($jsonResult, true)['success'];
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


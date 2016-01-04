<?php

//The path to the db-config file
defined('DB_FUNCTIONS') || define('DB_FUNCTIONS', __DIR__ . '/DB_FUNCTIONS.php');

// import database connection variables
require_once DB_FUNCTIONS;
// attributes
// (name, typ)
$values = array(
    'atr1' => 'aufg_name',
    'atr2' => 'parz_id'
);

$jsonResult = insertCall('insertAufg_gehoert_zu_parz', $values);

//only print result if this file is not included (part of a transaction)
if(!isset($printDirect)){
    echo $jsonResult;
}

<?php

//Path to the 'db_connection'-file
defined('DB_FUNCTIONS') || define('DB_FUNCTIONS', __DIR__ . '/DB_FUNCTIONS.php');

//JSON response
require_once DB_FUNCTIONS;
// attributes
// (name, typ)
$values = array(
    'atr1' => 'eig_name'
);

$jsonResult = insertCall('insertEigenschaft', $values);

//only print result if this file is not included (part of a transaction)
if(!isset($printDirect)){
    echo $jsonResult;
}

<?php

//The path to the db-config file
defined('DB_FUNCTIONS') || define('DB_FUNCTIONS', __DIR__ . '/DB_FUNCTIONS.php');

// import database connection variables
require_once DB_FUNCTIONS;

$jsonResult = insertCall('insertFeld');

//only print result if this file is not included (part of a transaction)
if(!isset($printDirect)){
    echo $jsonResult;
}
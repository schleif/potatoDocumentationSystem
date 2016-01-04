<?php

//The path to the db-config file
define('DB_FUNCTIONS', __DIR__ . '/DB_FUNCTIONS.php');

// import database connection variables
require_once DB_FUNCTIONS;

$params = array("feld_nr", "parz_row");

select("selectParzellenByRow", $params);
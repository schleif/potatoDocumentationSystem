<?php

//Path to the 'db_connection'-file
define('DB_FUNCTIONS', __DIR__ . '/DB_FUNCTIONS.php');


//JSON response
require_once DB_FUNCTIONS;
// attributes
// (name, typ)
$values = array(
    'atr1' => 'eig_name'
);

insertCall('insertEigenschaft', $values);

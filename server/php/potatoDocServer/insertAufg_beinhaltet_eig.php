<?php

//The path to the db-config file
define('DB_FUNCTIONS', __DIR__ . '/DB_FUNCTIONS.php');

// import database connection variables
require_once DB_FUNCTIONS;
// attributes
// (name, typ)
$values = array(
    'atr1' => 'aufg_name',
    'atr2' => 'eig_name'
);

insert('aufg_beinhaltet_eig', $values);

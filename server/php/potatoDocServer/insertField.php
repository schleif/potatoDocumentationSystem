<?php

//The path to the db-config file
define('DB_FUNCTIONS', __DIR__ . '/DB_FUNCTIONS.php');

// import database connection variables
require_once DB_FUNCTIONS;
// attributes
// (name, typ)
$values = array(
     'field' => 'field_id',
);

insert('field', $values);
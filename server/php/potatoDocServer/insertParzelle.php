<?php

//The path to the db-config file
define('DB_FUNCTIONS', __DIR__ . '/DB_FUNCTIONS.php');

// import database connection variables
require_once DB_FUNCTIONS;
// attributes
// (name, typ)
$values = array(
    // 'atr1' => 'parz_id',
    'atr2' => 'feld_nr',
    'atr3' => 'sorte'
);

insert('parzellen', $values);

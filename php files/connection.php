<?php
header("content-Type: application/json");
define('DB_USER', "root");
define('DB_PASSWORD', "");
define('DB_DATABASE', "task_manager");
define('DB_SERVER', "localhost");

$connection = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE);

$response = array();

if(mysqli_connect_errno())
{
   //  echo "Failed to connect to MySQL: " . mysqli_connect_error();

     $response['status'] = 1;
     $response['message'] = "failed to connection";
}
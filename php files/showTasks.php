<?php

include_once 'connection.php';
include_once 'operations.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST'){
       showTask();
}else{
    $response['status'] = 1;
    $response['message'] = "error for show tasks";
}
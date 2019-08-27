<?php
include_once 'connection.php';
include_once 'operations.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON , true);

if ($_SERVER['REQUEST_METHOD'] == 'POST'){
    if (isset($input['usernameOrEmail']) && isset($input['title'])){

        $usernameOrEmail = mysqli_escape_string($connection , $input["usernameOrEmail"]);
        $title = mysqli_escape_string($connection , $input["title"]);

        deleteTaskFromList($usernameOrEmail, $title);
    }
}

echo json_encode($response);
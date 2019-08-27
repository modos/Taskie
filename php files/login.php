<?php
include_once 'connection.php';
include_once 'operations.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON , true);

if ($_SERVER['REQUEST_METHOD'] == 'POST'){
    if (isset($input['usernameOrEmail']) && isset($input['password'])){

        $usernameOrEmail = mysqli_escape_string($connection , $input["usernameOrEmail"]);
        $passsword = mysqli_escape_string($connection , $input["password"]);

        loginUser($usernameOrEmail, $passsword);
    }
}

echo json_encode($response);
<?php

include_once 'connection.php';
include_once 'operations.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON , true);

if ($_SERVER['REQUEST_METHOD'] == 'POST'){
    if (isset($input['usernameOrEmail']) && isset($input['title']) && isset($input['description'])
        && isset($input['year']) && isset($input['month']) && isset($input['day'])
        && isset($input['hour']) && isset($input['month'])){

        $usernameOrEmail = mysqli_escape_string($connection , $input["usernameOrEmail"]);
        $title = mysqli_escape_string($connection , $input["title"]);
        $description = mysqli_escape_string($connection , $input["description"]);
        $year = mysqli_escape_string($connection , $input["year"]);
        $month = mysqli_escape_string($connection , $input["month"]);
        $day = mysqli_escape_string($connection , $input["day"]);
        $hour = mysqli_escape_string($connection , $input["hour"]);
        $minute = mysqli_escape_string($connection , $input["minute"]);

        createTask($usernameOrEmail,  $title, $description, $year, $month, $day, $hour, $minute);
    }
}

echo json_encode($response);
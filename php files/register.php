<?php
ob_clean();

include_once 'connection.php';
include_once 'operations.php';


$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON , true);

   if ($_SERVER['REQUEST_METHOD'] == 'POST'){
       if (isset($input['username']) && isset($input['password']) && isset($input['email'])
           && isset($input['role'])){

           $username = mysqli_escape_string($connection , $input["username"]);
           $passsword = mysqli_escape_string($connection , $input["password"]);
           $email = mysqli_escape_string($connection , $input["email"]);
           $role = mysqli_escape_string($connection , $input["role"]);

           addUser($username, $passsword, $email, $role);
       }
   }

echo json_encode($response);
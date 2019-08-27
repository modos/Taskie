<?php
ob_clean();

function isUserExist($username, $email){
    $query1 = "SELECT username FROM users WHERE username = ?";
    $query2 = "SELECT email FROM users WHERE email = ?";

    global $connection;
    global $response;

    if ($stmt= $connection->prepare($query1)){
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $stmt->store_result();
        $stmt->fetch();

        if ($stmt->num_rows == 1){
            $stmt->close();

            $response['status'] = 1;
            $response['message'] = "user already exist";

            return true;
        }
    }else if ($stmt= $connection->prepare($query2)){
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();
        $stmt->fetch();

        if ($stmt->num_rows == 1){
            $stmt->close();

            $response['status'] = 1;
            $response['message'] = "user already exist";

            return true;
        }
    }

    return false;
}

function addUser($username, $password, $email, $role){
    $query = "INSERT INTO users(username, password, email, role) VALUES (?,?,?,?)";

    global $connection;
    global $response;

    if (!isUserExist($username, $email)){
        if ($stmt=$connection->prepare($query)){
            $stmt->bind_param("ssss", $username, $password, $email, $role);
            $stmt->execute();

            $stmt->close();

            $response['status'] = 0;
            $response['message'] = "user created successfully";

        }else {
            $response['status'] = 1;
            $response['message'] = "error occurred";
            $stmt->close();
        }
    }else{
        $response['status'] = 1;
        $response['message'] = "user exist";
    }

}

function loginUser($usernameOrEmail, $password){
    $query1 = "SELECT username FROM users WHERE password = ? AND username = ?";
    $query2 = "SELECT email FROM users WHERE password = ? AND email = ?";

    global $connection;
    global $response;

    if ($stmt=$connection->prepare($query1)){
        $stmt->bind_param("ss", $password, $usernameOrEmail);
        $stmt->execute();
        $stmt->store_result();
        $stmt->fetch();

        if ($stmt->num_rows() == 1){
            $response['status'] = 0;
            $response['message'] = "logged in successfully";
            $stmt->close();
        }else if($stmt=$connection->prepare($query2)){
        $stmt->bind_param("ss", $password, $usernameOrEmail);
        $stmt->execute();
        $stmt->store_result();
        $stmt->fetch();

        if ($stmt->num_rows() == 1){
            $response['status'] = 0;
            $response['message'] = "logged in successfully";
            $stmt->close();
        }else{
            $response['status'] = 1;
            $response['message'] = "username, email or password is not correct";
            $stmt->close();
        }
        }}else{
        $response['status'] = 1;
        $response['message'] = "error occurred";}}

function createTask($usernameOrEmail, $title, $description, $year, $month, $day, $hour, $minute){
    global $connection;
    global $response;

    $findUser = "SELECT username, email FROM users WHERE username = ? OR email = ?";
    $username = "";
    $email = "";

    if ($stmt = $connection->prepare($findUser)){
        $stmt->bind_param("ss", $usernameOrEmail, $usernameOrEmail);
        $stmt->execute();
        $result = $stmt->get_result();
        $row= $result->fetch_assoc();
        $username = $row['username'];
        $email = $row['email'];

    }

    $query = "INSERT INTO tasks(username, email, title, description, year, month, day, hour, minute)
              VALUES (?,?,?,?,?,?,?,?,?)";

    if($stmt = $connection->prepare($query)){
        $stmt->bind_param("ssssddddd" , $username, $email, $title, $description, $year, $month, $day, $hour, $minute);

        if ($stmt->execute()){
            $response['status'] = 0;
            $response['message'] = "task created successfully";
        }else{
            $response['status'] = 1;
            $response['message'] = "some error occured";
        }
    }else{
        $response['status'] = 1;
        $response['message'] = "some error occured";
    }
}

function deleteUser($usernameOrEmail){
    $query = "DELETE FROM users WHERE username = ? OR email = ?";
    $query1 = "DELETE FROM tasks WHERE username = ? OR email = ?";

    global $connection;
    global $response;

    if ($stmt = $connection->prepare($query)){
        $stmt->bind_param("ss", $usernameOrEmail , $usernameOrEmail);
        if($stmt->execute()){
            $response['status'] = 0;
            $response['message'] = "user deleted successfully";
        }else{
            $response['status'] = 1;
            $response['message'] = "some errors occurred";
        }
    }else{
        $response['status'] = 1;
        $response['message'] = "some errors occurred";
    }

    if ($stmt = $connection->prepare($query1)){
        $stmt->bind_param("ss", $usernameOrEmail , $usernameOrEmail);
        $stmt->execute();
    }


}

function changePassword($usernameOrEmail, $password){
    $query = "UPDATE users SET password = ? WHERE username = ? OR email = ?";

    global $connection;
    global $response;

    if ($stmt = $connection->prepare($query)){
        $stmt->bind_param("sss", $password, $usernameOrEmail, $usernameOrEmail);

        if ($stmt->execute()){
            $response['status'] = 0;
            $response['message'] = "password change successfully";
        }else{
            $response['status'] = 1;
            $response['message'] = "some error ouccured";
        }
    }
}

function showTask(){
    global $connection;

    $query = "SELECT username, email, title, description, year, month, day, hour, minute FROM tasks ORDER BY 
              year, month, day, hour, minute";

    if ($stmt = $connection->prepare($query)){
        $stmt->execute();
        $return = $stmt->get_result();
        $dbdata = array();

        while ($row = $return->fetch_assoc())  {
            $dbdata[]=$row;
        }

        echo json_encode($dbdata);
    }
}

function deleteTaskFromList($usernameOrEmail, $title){
    global $connection;
    global $response;

    $query = "DELETE FROM tasks WHERE  (username = ? OR email = ?) AND title = ?";

    if ($stmt = $connection->prepare($query)){
        $stmt->bind_param("sss", $usernameOrEmail, $usernameOrEmail, $title);
        if ($stmt->execute()){
            $response['status'] = 0;
            $response['message'] = "task deleted successfully";
        }else{
            $response['status'] = 1;
            $response['message'] = "failed";
        }
    }else{
        $response['status'] = 1;
        $response['message'] = "somthing wrong";
    }
}
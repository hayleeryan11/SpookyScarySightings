<?php

/*
 * This file provides a web service to add a user to the database. Given an "email", "password",
 *  "question" and "answer," attempts to add user info to database. Returns success message if successful,
 *  or returns an error with error message.
 */

ini_set('display_errors', '1');
error_reporting(E_ALL);

 
require 'dbConnect.php';
    
//get input 
$first = isset($_GET['first']) ? $_GET['first'] : '';
$last = isset($_GET['last']) ? $_GET['last'] : '';
$username = isset($_GET['username']) ? $_GET['username'] : '';
$email = isset($_GET['email']) ? $_GET['email'] : '';
$password = isset($_GET['password']) ? $_GET['password'] : '';


$result = mysql_query($sql = "SELECT * FROM Users WHERE username = $username");
//validate input
if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    echo '{"result": "fail", "error": "Please enter a valid email."}';
} else if (strlen($password) < 6) {
    echo '{"result": "fail", "error": "Please enter a valid password (longer than five characters)."}';
} else if ($result && mysql_num_rows($result) <= 0) {
	echo '{"result": "fail", "error": "Username already taken."}';
} else {    
    //build query
    $sql = "INSERT INTO Users";
    $sql .= " VALUES ('$first', '$last', '$username', '$password', $email')";
       
   try { 
		
		//attempts to add record
		if ($result = $db->query($sql)) {
			echo '{"result": "success"}';
			$db = null;
		} 
   } catch(PDOException $e) {
		if ((int)($e->getCode()) == 23000) {
			echo '{"result": "fail", "error": "That email address has already been registered."}';
		} else {
			//echo 'Error Number: ' . $e->getCode() . '<br>';
			echo '{"result": "fail", "error": "Unknown error (' . (((int)($e->getCode()) + 123) * 2) .')"}';
			//on error, return error message in json
		}
    }
}
?>
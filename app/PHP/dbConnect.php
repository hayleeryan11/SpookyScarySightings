<?php
	ini_set('display_errors', '1');
	error_reporting(E_ERROR | E_PARSE);

	// Connect to the 000webhost Database
	// TO DO - These are my values - Change to yours
	$dsn = 'mysql:host=localhost;dbname=id5577748_spookyscary';
	$username = 'id5577748_spookyscary';
	$password = 'eelyah';

	// Connect to the cssgate Database
	// TO DO - Uncomment and change uwnetid and mysqlpassword to yours
	//$dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=uwnetid';
    //$username = 'uwnetid';
    //$password = 'mysqlpassword';

	$db = null;

	try {

    	$db = new PDO($dsn, $username, $password);

	} catch (PDOException $e) {
    	$error_message = $e->getMessage();
    	echo 'There was an error connecting to the database.';
		echo $error_message;
    	exit();
	}
?>

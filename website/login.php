<?php

   /*-----------------------------------------------------------------------------------------------
--    Name:     [InitializeSocket]         Date:         [March 18th, 2016]
--
--    Designer: [Vivek Kalia]                Programmer:   [Vivek Kalia]
--
--    Interface: N/A 
--
--
--    Notes: This is the authentication page to check whether you are allowed in to view 
--            the map.
------------------------------------------------------------------------------------------------*/
  

   // First check to see if they are logged in.
   // If they are we take them back to the map page 
   // Since they do not need to be here.
   session_start();
   if(isset($_SESSION['logged_in']))
   {
      header("Location: index.php");
      exit();
   }
?>
<!DOCTYPE html>
<!-- SIGNIN TEMPLATE COURTESY OF BOOTSTRAP -->
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Sign-In</title>

    <!-- Bootstrap core CSS -->
    <!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">    


    <!-- Custom styles for this template -->
    <link href="styles/signin.css" rel="stylesheet">


    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <div class="container">

      <form class="form-signin" action="" method="POST">
        <h2 class="form-signin-heading">Location Tracker</h2>
        <label for="inputUsername" class="sr-only">Username</label>
        <input type="text" name="inputUsername" id="inputUsername" class="form-control" placeholder="Username" required autofocus>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" name="inputPassword" id="inputPassword" class="form-control" placeholder="Password" required>
        <button id="loginBtn" name="loginBtn" class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
      </form>

    </div> <!-- /container -->
  </body>
</html>

<?php
   if( isset($_POST['loginBtn']) ) 
   {
        $username = trim($_POST['inputUsername']);
	$password = trim($_POST['inputPassword']);
        
        // Try to connect to database
	try{
		$db = new PDO('mysql:host=localhost;dbname=android_gps;', 'root', 'c0mmaudi0');
	} catch (PDOException $e) {
		echo 'failure!';
	} 
        
        // We query the database with users input, making sure to sanitize along the way with the use
	// of'prepare'. If there is a match for a username and password pair then the user is logged in.
	$query = $db->prepare("SELECT * FROM users WHERE username=? AND password=?");
	$query->bindValue(1, $username);
	$query->bindValue(2, $password);
        $query->execute(); 
	if($query->rowCount() > 0)
	{
	   $result = $query->fetch();
	   $_SESSION['logged_in'] = TRUE;
	   $_SESSION['id'] = $result['user_id'];
	   header("Location: index.php");
	} 
   } 

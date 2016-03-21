<?php
/*---------------------------------------------------------------------------------------
--  Source File:    grab_newest_user.php - PHP file to grab the latest entry from the
--                                         location
--
--  Date:         March 19, 2016
--
--  Revisions:    March 20, 2016 (Tyler Trepanier) 
--                    Final integration, allowed for continuous updates
--
--  Designer:     Tyler Trepanier
--
--  Programmer:   Tyler Trepanier
--
--  Notes: Single PHP file which grabs all clients and returns it to the
--  mapcalls.js function via JSON.
---------------------------------------------------------------------------------------*/
    
    $servername = "localhost";
    $username = "root";
    $password = "c0mmaudi0";
    $my_db = "android_gps";
    $arr = [];
    
    try {
        // Create connection
        $conn = new mysqli($servername, $username, $password, $my_db);
        
        // Check connection
        if ($conn->connect_error) {
            die('Connect Error (' . $conn->connect_errno . ') '
            . $conn->connect_error);
        }
        
        $sql = "SELECT  u.username, l.rec_time, l.latitude, l.longitude, l.dev_name, l.ip_address 
                FROM    location l
                INNER JOIN    users u
                ON u.user_id=l.user_id";        
        
        if ($result = $conn->query($sql)) {
            while($r = $result->fetch_array(MYSQLI_NUM)) {
                $arr[] = $r;
            }
        }
        
        $conn->close();
        
        } catch (Exception $e) {
        echo 'Caught exception: ',  $e->getMessage(), "\n";    
    }
    
    echo json_encode($arr);
?>

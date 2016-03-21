<?php
    // This php file selects the most recent users time, location, device and their ip_address, convert it to JSON data and push it back to our Client
    
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
                ON u.user_id = l.user_id
                ORDER BY l.rec_time DESC
                LIMIT 1";        
        
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

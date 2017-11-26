<?php

   $conn=mysqli_connect("localhost","id3502629_root","cwyroot2017","id3502629_cwy") or die("Error " . mysqli_error($con));

    
    $where = "";
    if(isset($_POST["id_incidente"])){
    	$where = "WHERE id = ".$_POST["id_incidente"];
        $sql = "DELETE FROM imagenes WHERE id_incidente = ".$_POST["id_incidente"];
        $result = mysqli_query($conn, $sql) or die("Error in Selecting " . mysqli_error($conn));
        $sql = "DELETE FROM incidentes ".$where; 
        $result = mysqli_query($conn, $sql) or die("Error in Selecting " . mysqli_error($conn));
    }

?>
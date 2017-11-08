<?php

   $conn=mysqli_connect("localhost","id3502629_root","cwyroot2017","id3502629_cwy") or die("Error " . mysqli_error($con));


 	//fetch table rows from mysql db
	$sql = "SELECT * FROM incidentes WHERE zona = '".$_POST["zona"]."'";
	$result = mysqli_query($conn, $sql) or die("Error in Selecting " . mysqli_error($conn));

	 //create an array
    $emparray = array();
    while($row =mysqli_fetch_assoc($result))
    {
        $emparray[] = $row;
    }
     echo json_encode($emparray);
?>
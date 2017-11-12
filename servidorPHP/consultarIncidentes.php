<?php

   $conn=mysqli_connect("localhost","id3502629_root","cwyroot2017","id3502629_cwy") or die("Error " . mysqli_error($con));


//$_POST["zona"]
    //$zona = "PODRIAN SER COORDENADAS";
 	//fetch table rows from mysql db
 	
 	//para que traiga la fecha en español
 	mysqli_query($conn, "SET lc_time_names = 'es_AR'");
 	
	$sql = "SELECT COUNT(*) as cantidad,tipo,zona FROM incidentes GROUP BY tipo HAVING zona = '".$_POST["zona"]."'";
	$result = mysqli_query($conn, $sql) or die("Error in Selecting " . mysqli_error($conn));

	 //create an array
    $emparray = array();
    while($row =mysqli_fetch_assoc($result))
    {
        array_push($emparray,$row);
    }

    mysqli_query($conn, "SET lc_time_names = 'es_AR'");
    
    $sql = "SELECT id,usuario,DATE_FORMAT(fechayhora,'%d %M %Y  %h:%m:%s') fechaYhora,tipo,zona,comentarios FROM incidentes WHERE zona = '".$_POST["zona"]."'";
	$result = mysqli_query($conn, $sql) or die("Error in Selecting " . mysqli_error($conn));

    $sinagrupararray = array();
    while($row =mysqli_fetch_assoc($result))
    {
        array_push($sinagrupararray,$row);
    }
    $respuesta = new stdClass();
    $respuesta->agrupada = $emparray;
    $respuesta->sinagrupar = $sinagrupararray;
     echo json_encode($respuesta);
?>
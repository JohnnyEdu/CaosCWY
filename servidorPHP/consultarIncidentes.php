<?php

   $conn=mysqli_connect("localhost","id3502629_root","cwyroot2017","id3502629_cwy") or die("Error " . mysqli_error($con));


//$_POST["zona"]
    //$zona = "PODRIAN SER COORDENADAS";
 	//fetch table rows from mysql db
 	
 	//para que traiga la fecha en español
 	mysqli_query($conn, "SET lc_time_names = 'es_AR'");
 	/***************Se comenta esta consulta, ya que el agrupamiento lo hacemos en android ***********/
	/*$sql = "SELECT COUNT(*) as cantidad,tipo,zona,latitud,longitud FROM incidentes GROUP BY tipo,latitud,longitud HAVING latitud = ".$_POST["latitud"]." AND longitud = ".$_POST["longitud"];
	$result = mysqli_query($conn, $sql) or die("Error in Selecting " . mysqli_error($conn));*/

	 //create an array
    //$emparray = array();
    /*while($row =mysqli_fetch_assoc($result))
    {
        array_push($emparray,$row);
    }*/

    mysqli_query($conn, "SET lc_time_names = 'es_AR'");
    
    $where = "";
    if(isset($_POST["usuario"])){
    	$where = "WHERE usuario = ".$_POST["usuario"];
    }else{
	    //-35, -58
	$latitudArea = floatval($_POST["latitud"]);
	$longitudArea = floatval($_POST["longitud"]);
    	$where = " WHERE i.latitud  BETWEEN  (".$_POST["latitud"]." -200) AND (".$_POST["longitud"]."+200) AND i.longitud BETWEEN
	(".$_POST["longitud"]."-200) AND (".$_POST["latitud"]." +200");
    }
    //$sql = "SELECT incs.id,incs.usuario,DATE_FORMAT(fechayhora,'%d %M %Y  %h:%m:%s') fechaYhora,tipo,zona,comentarios,latitud,longitud,imgs.imagen FROM incidentes incs JOIN imagenes imgs ON imgs.id_incidente = incs.id".$where; 

	$sql = " SELECT i.id,i.usuario, DATE_FORMAT(i.fechayhora,'%d %M %Y  %h:%m:%s') as fechaYhora, i.tipo, i.zona, i.comentarios, i.latitud, i.longitud, im.imagen 
FROM incidentes i 
inner JOIN imagenes im ON im.id_incidente = i.id"

	$result = mysqli_query($conn, $sql) or die("Error in Selecting " . mysqli_error($conn));

    $sinagrupararray = array();
    while($row =mysqli_fetch_assoc($result))
    {
        array_push($sinagrupararray,$row);
    }
    $respuesta = new stdClass();
    //$respuesta->agrupada = $emparray;
    $respuesta->sinagrupar = $sinagrupararray;
     echo json_encode($respuesta);
?>

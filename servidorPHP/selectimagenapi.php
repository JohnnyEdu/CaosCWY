<?php 

	$conn = new mysqli("localhost", "id3502629_root", "cwyroot2017", "id3502629_cwy");
	// Check connection
	if ($conn->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	} 
	if(isset($_POST["usuario"])){
	    echo $_POST["usuario"];
		$sql = "SELECT usuario, imagen FROM imagenes WHERE usuario = '".$_POST["usuario"]."'";	
	}else{
		$sql = "SELECT usuario, imagen FROM imagenes";
	}

//fetch table rows from mysql db
	$result = mysqli_query($conn, $sql) or die("Error in Selecting " . mysqli_error($conn));

	 //create an array
    $emparray = array();
    while($row =mysqli_fetch_assoc($result))
    {
        $emparray[] = $row;
    }
     echo json_encode($emparray);
?>

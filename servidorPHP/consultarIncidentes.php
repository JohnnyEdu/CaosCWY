<?php

   $con=mysqli_connect("localhost","id3502629_root","cwyroot2017","id3502629_cwy");
  
//  $sql="INSERT INTO incidentes (usuario,fechaYhora,tipo,zona) VALUES (?,?,?,?)";
 // $sql->bind_param($_POST["usuario"],$_POST["fechaYhora"],$_POST["tipo"],$_POST["zona"]);
  
$sth = $con->query("SELECT * FROM incidentes WHERE zona = '".$_POST["zona"]."'");
$rows = array();
while($r = mysqli_fetch_assoc($sth)) {
    $rows[] = $r;
}
echo json_encode($rows);
?>
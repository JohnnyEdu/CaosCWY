<?php

   $con=new mysqli("localhost","id3502629_root","cwyroot2017","id3502629_cwy");
   
   
   /* verificar conexión */
    if (mysqli_connect_errno()) {
        printf("Error de conexión: %s\n", mysqli_connect_error());
        exit();
    }
  
  $usuario = $_POST["usuario"];
  $tipo = $_POST["tipo"];
  $zona = $_POST["zona"];
  $comentario = $_POST["comentario"];
  $latitud = $_POST["latitud"];
  $longitud = $_POST["longitud"];
  
 
$stmt = $con->prepare("INSERT INTO incidentes (usuario,fechaYhora,tipo,zona,comentarios,latitud,longitud) VALUES (?,NOW(),?,?,?,?,?)");
$stmt->bind_param("ssssss",$usuario,$tipo,$zona,$comentario,$latitud,$longitud);

/* ejecuta sentencias prepradas */

$ejecuto = $stmt->execute();

$stmt->close();
   if ($ejecuto === true) {
      echo "Se insertó el incidente en la BD";
   }else{
      echo "No se pudo insertar";
   }
   
   if(isset($_POST["imagen"])){
       $id_incidente = $con->insert_id;
       
       $imagen = $_POST["imagen"];
        $sql="INSERT INTO imagenes (usuario,imagen,id_incidente) VALUES ('".$usuario."','".$imagen."',".$id_incidente.")";
    
       if (mysqli_query($con,$sql)===true) {
          echo "Se insertó la imagen en la BD";
       }else{
          echo "No se pudo insertar";
       }
   }
?>
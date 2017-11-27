<?php

   $con=new mysqli("localhost","id3502629_root","cwyroot2017","id3502629_cwy");
   
   
   /* verificar conexión */
    if (mysqli_connect_errno()) {
        printf("Error de conexión: %s\n", mysqli_connect_error());
        exit();
    }
  
  $id = $_POST["id"];
  $usuario = $_POST["usuario"];
  $tipo = $_POST["tipo"];
  $zona = $_POST["zona"];
  $comentario = $_POST["comentario"];
  $latitud = $_POST["latitud"];
  $longitud = $_POST["longitud"];
  
 
$stmt = $con->prepare("UPDATE incidentes SET usuario = ?,fechaYhora = NOW(),tipo = ?,zona =?,comentarios = ? ,latitud = ?,longitud = ? WHERE id = ?");
$stmt->bind_param("sssssss",$usuario,$tipo,$zona,$comentario,$latitud,$longitud,$id);

/* ejecuta sentencias prepradas */

$ejecuto = $stmt->execute();

$stmt->close();
   if ($ejecuto === true) {
      echo "Se actualizo  incidente en la BD";
   }else{
      echo "No se pudo actualizar";
   }
   
   if(isset($_POST["imagen"])){
       
       mysqli_query($con,"DELETE FROM imagenes WHERE id_incidente = ".$id);
       $imagen = $_POST["imagen"];
         $sql="INSERT INTO imagenes (usuario,imagen,id_incidente) VALUES ('".$usuario."','".$imagen."',".$id.")";
    
       if (mysqli_query($con,$sql)===true) {
          echo "Se insertó la imagen en la BD";
       }else{
          echo "No se pudo insertar";
       }
   }
?>
<?php

   $con=new mysqli("localhost","id3502629_root","cwyroot2017","id3502629_cwy");
   
   
   /* verificar conexión */
    if (mysqli_connect_errno()) {
        printf("Error de conexión: %s\n", mysqli_connect_error());
        exit();
    }
  
//  $sql="INSERT INTO incidentes (usuario,fechaYhora,tipo,zona) VALUES (?,?,?,?)";
 // $sql->bind_param($_POST["usuario"],$_POST["fechaYhora"],$_POST["tipo"],$_POST["zona"]);
  
  
  $stmt = $con->prepare("INSERT INTO incidentes (usuario,fechaYhora,tipo,zona,comentarios) VALUES (?,NOW(),?,?,?)");
$stmt->bind_param("sss",$_POST["usuario"],$_POST["tipo"],$_POST["zona"],$_POST["comentario"]);

/* ejecuta sentencias prepradas */

echo "insertando ";
$ejecuto = $stmt->execute();

$stmt->close();
echo $ejecuto;
echo "se termino la consulta";
   if ($ejecuto) {
      echo "Se insertó el incidente en la BD";
   }else{
      echo "No se pudo insertar";
   }
?>
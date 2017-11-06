<?php

   $con=mysqli_connect("localhost","id3502629_root","cwyroot2017","id3502629_cwy");
   /*$sql="DELETE FROM imagenes WHERE usuario IN('".$_POST["usuario"]."')";
   echo $sql;
   if(mysqli_query($con,$sql)){
      echo "Se eliminó el registro para ese usuario";
   }else{
      echo "No se pudo borrar";
   }*/

   $sql="INSERT INTO imagenes (usuario,imagen) VALUES ('".
               $_POST["usuario"]."',". $_POST["imagen"]. ")";
                $sql="INSERT INTO imagenes (usuario,imagen) VALUES ('".
               $_POST["usuario"]."',NULL)";
   echo $sql;
   if (mysqli_query($con,$sql)) {
      echo "Se insertó la imagen en la BD";
   }else{
      echo "No se pudo insertar";
   }
?>
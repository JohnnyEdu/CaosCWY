<?php
   //$con=mysqli_connect("localhost","id3502629_root","cwyroot2017");

   //mysqli_query($con,"DROP DATABASE cwy");

   /*$sql="CREATE DATABASE cwy";
   if (mysqli_query($con,$sql)) {
      echo "Se creo la base de datos";
   }*/
   $con=mysqli_connect("localhost","id3502629_root","cwyroot2017","id3502629_cwy");
   

   mysqli_query($con,"DROP TABLE usuarios");
   $sql="CREATE TABLE usuarios(usuario VARCHAR(100) PRIMARY KEY,password VARCHAR(255))";
   if (mysqli_query($con,$sql)) {
      echo "Se creó la tabla USUARIOS";
   }
   mysqli_query($con,"DROP TABLE imagenes");
   $sql="CREATE TABLE imagenes(id INT(4) PRIMARY KEY AUTO_INCREMENT, usuario VARCHAR(100),imagen MEDIUMTEXT CHARACTER SET ascii, id_incidente INT(4), FOREIGN KEY(id_incidente) REFERENCES incidentes(id) ON UPDATE CASCADE ON DELETE RESTRICT)";
   if (mysqli_query($con,$sql)) {
      echo "Se creó la tabla IMAGENES";
   }

   mysqli_query($con,"DROP TABLE incidentes");
   $sql="CREATE TABLE incidentes(id INT(4) PRIMARY KEY AUTO_INCREMENT,usuario VARCHAR(100),fechaYhora DATETIME, tipo VARCHAR(60),zona VARCHAR(100),comentarios VARCHAR(500))";
   if (mysqli_query($con,$sql)) {
      echo "Se creó la tabla INCIDENTES";
   }
?>

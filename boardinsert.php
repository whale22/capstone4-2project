<?php
$id=$_POST['id'];
$title=$_POST['title'];
$cont=$_POST['content'];
$link = mysqli_connect("localhost","root","1111","test");
mysqli_query($link,"SET NAMES utf8");
if(!$link){
	die('Could not connect:'.mysql_error());
}
// if connection failed, echo message
$sql="INSERT INTO board_info (id, title, content) VALUES ('".$id."', '".$title."', '".$cont."');";
$ok=mysqli_query($link,$sql);

if($ok==false) print("false");
else print("true");
// if connection failed, echo message

mysqli_close($link);


?>
<?php
$id=$_POST['id'];
$title=$_POST['title'];
$cont=$_POST['comment'];
$link = mysqli_connect("localhost","root","1111","test");
mysqli_query($link,"SET NAMES utf8");
if(!$link){
	die('Could not connect:'.mysql_error());
}
// if connection failed, echo message
$sql="INSERT INTO board_comment (title, id, comment) VALUES ('".$title."', '".$id."', '".$cont."');";
$ok=mysqli_query($link,$sql);

if($ok==false) print("false");
else print("true");
// if connection failed, echo message

mysqli_close($link);


?>
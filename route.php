<?php
$name=$_POST['name'];
$rn=$_POST['random'];
$time=$_POST['time'];
$timestamp=$_POST['datetime'];
$memo=$_POST['memo'];
$link = mysqli_connect("localhost","root","1111","test");
mysqli_query($link,"SET NAMES utf8");
if(!$link){
	die('Could not connect:'.mysql_error());
}
// if connection failed, echo message
$sql="INSERT INTO route_register (route_num, name, time, timestamp, memo) VALUES ('".$rn."', '".$name."', '".$time."', '".$timestamp."', '".$memo."');";
$ok=mysqli_query($link,$sql);

if($ok==false) print("false");
else print("true");
// if connection failed, echo message

mysqli_close($link);


?>
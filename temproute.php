<?php
$rn=$_POST['random'];
$lat=$_POST['latitude'];
$long=$_POST['longitude'];
$link = mysqli_connect("localhost","root","1111","test");
mysqli_query($link,"SET NAMES utf8");
if(!$link){
	die('Could not connect:'.mysql_error());
}
// if connection failed, echo message
$sql="INSERT INTO temproute (route_num, latitude, longitude) VALUES ('".$rn."', '".$lat."', '".$long."');";
$ok=mysqli_query($link,$sql);

if($ok==false) print("false");
else print("true");
// if connection failed, echo message

mysqli_close($link);


?>
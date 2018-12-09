<?php
$at=$_POST['alarm_type'];
$locx=$_POST['location_x'];
$locy=$_POST['location_y'];
$docs=$_POST['dog_size'];
$docc=$_POST['dog_color'];
$doct=$_POST['dog_type'];
$info=$_POST['information'];
$link = mysqli_connect("localhost","root","1111","test");
mysqli_query($link,"SET NAMES utf8");
if(!$link){
	die('Could not connect:'.mysql_error());
}
// if connection failed, echo message
$sql="INSERT INTO tempalarm (Alarm_type, Location_x,location_y, Dog_size, Dog_color,Dog_type, Information) VALUES ('".$at."', '".$locx."', '".$locy."', '".$docs."', '".$docc."', '".$doct."','".$info."');";
$ok=mysqli_query($link,$sql);

if($ok==false) print("false");
else print("true");
// if connection failed, echo message

mysqli_close($link);


?>
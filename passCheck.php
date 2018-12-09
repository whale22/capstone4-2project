<?php

$id=$_POST['id'];
$pass=$_POST['pass'];
if(isset($_POST['latitude'])) $locx=$_POST['latitude'];
else $locx=0;
if(isset($_POST['longitude'])) $locy=$_POST['longitude'];
else $locy=0;
$link = mysqli_connect("localhost","root","1111","test");
mysqli_query($link,"SET NAMES utf8");
if(!$link){
	die('Could not connect:'.mysql_error());
}
// if connection failed, echo message

$result=mysqli_query($link,"SELECT * FROM tempuser WHERE id='".$id."';");
$row=mysqli_fetch_array($result);
//echo $row['pass']; // get pass
//$ok=mysqli_query($link,"INSERT INTO test ('pass') VALUES ('000000')");
//pass insert need to modify
if($pass==$row['Password']) {
	print(json_encode("true"));
	print(":".$row['Id']);
	$sql="UPDATE tempuser SET location_x=".$locx.",location_y=".$locy." WHERE id='".$id."';";
mysqli_query($link,$sql);
}else print(json_encode("false"));
// if connection failed, echo message

mysqli_close($link);


?>

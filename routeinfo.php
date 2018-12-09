<?php
$link = mysqli_connect("localhost","root","1111","test");
if(!$link){
	die('Could not connect:'.mysql_error());
}
mysqli_query($link,"SET NAMES utf8");
// if connection failed, echo message

$sql="SELECT * FROM route_register;";
$result=mysqli_query($link,$sql);
$flag=false;
while($row=mysqli_fetch_array($result)){
	if($flag==false){
		$flag=true;
	}else print ";";
	echo $row['name'].";".$row['time'].";".$row['timestamp'].";".$row['memo'].";".$row['route_num'];
}
// if connection failed, echo message

mysqli_close($link);


?>

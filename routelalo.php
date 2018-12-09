<?php
$rn=$_POST['random'];
$link = mysqli_connect("localhost","root","1111","test");
mysqli_query($link,"SET NAMES utf8");
if(!$link){
	die('Could not connect:'.mysql_error());
}
// if connection failed, echo message

$result=mysqli_query($link,"SELECT * FROM temproute WHERE route_num='".$rn."';");
//echo $row['pass']; // get pass
$flag=false;
while($row=mysqli_fetch_array($result)){
	if($flag==false){
		$flag=true;
	}else print ";";
	echo $row['latitude'].";".$row['longitude'];
}
// if connection failed, echo message

mysqli_close($link);


?>

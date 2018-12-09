<?php
$id=$_POST['id'];
$lat=$_POST['latitude'];
$lng=$_POST['longitude'];
$link = mysqli_connect("localhost","root","1111","test");
if(!$link){
	die('Could not connect:'.mysql_error());
}
mysqli_query($link,"SET NAMES utf8");
// if connection failed, echo message

$sql="SELECT *, ( 6371 * acos( cos( radians(location_x) ) * cos( radians( ".$lat." ) ) * cos( radians( ".$lng." ) - radians(location_y) ) + sin( radians(location_x) ) * sin( radians( ".$lat." ) ) ) ) AS distance FROM tempuser HAVING distance < 3 ORDER BY distance LIMIT 0 , 3;";
$result=mysqli_query($link,$sql);
$flag=false;
while($row=mysqli_fetch_array($result)){
	if($id==$row['Id']) continue;
	if($flag==false){
		$flag=true;
	}else print ":";
	echo $row['Id'];
}
// if connection failed, echo message

mysqli_close($link);


?>

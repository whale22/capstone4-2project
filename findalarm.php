<?php
$lat=$_POST['latitude'];
$lng=$_POST['longitude'];
$link = mysqli_connect("localhost","root","1111","test");
mysqli_query($link,"SET NAMES utf8");
if(!$link){
	die('Could not connect:'.mysql_error());
}
// if connection failed, echo message
$sql="SELECT *, ( 6371 * acos( cos( radians(location_y) ) * cos( radians( ".$lat." ) ) * cos( radians( ".$lng." ) - radians(location_x) ) + sin( radians(location_y) ) * sin( radians( ".$lat." ) ) ) ) AS distance FROM tempalarm HAVING distance < 3 ORDER BY distance LIMIT 0 , 3;";
$result=mysqli_query($link,$sql);

if(!$result) print("false");
else 
while($info=mysqli_fetch_array($result)){
echo $info['information']."<br>";
}
// if connection failed, echo message

mysqli_close($link);


?>
<?php

$lat=$_POST['latitude'];
$lng=$_POST['longitude'];
$link = mysqli_connect("localhost","root","1111","test");
if(!$link){
	die('Could not connect:'.mysql_error());
}
mysqli_query($link,"SET NAMES utf8");
// if connection failed, echo message

$sql="SELECT *, ( 6371 * acos( cos( radians(location_x) ) * cos( radians( ".$lat." ) ) * cos( radians( ".$lng." ) - radians(location_y) ) + sin( radians(location_x) ) * sin( radians( ".$lat." ) ) ) ) AS distance FROM tempalarm HAVING distance < 3 ORDER BY distance LIMIT 0 , 3;";
$result=mysqli_query($link,$sql);
$flag=false;
while($row=mysqli_fetch_array($result)){
	if($flag==false){
		$flag=true;
	}else print ":";
	if($row['alarm_Type']==1) print "근처에 비매너 행동을 하는 주인이 있습니다.";
	else if($row['alarm_Type']==2) print "현재 산책로의 청결 상태가 좋지 않습니다.";
	else if($row['alarm_Type']==3) print "근처에 공격형 반려견이 있습니다. 조심하세요.";
	else if($row['alarm_Type']==4) print "근처에".$row['dog_size']." ".$row['dog_color']." ".$row['Dog_type']."의 유기 반려견이 있는 것으로 추정됩니다.";
	echo $row['Information'];
}
// if connection failed, echo message

mysqli_close($link);


?>

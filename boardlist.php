<?php
$link = mysqli_connect("localhost","root","1111","test");
if(!$link){
	die('Could not connect:'.mysql_error());
}
mysqli_query($link,"SET NAMES utf8");
// if connection failed, echo message

$sql="SELECT * FROM board_info;";
$result=mysqli_query($link,$sql);
$flag=false;
while($row=mysqli_fetch_array($result)){
	if($flag==false){
		$flag=true;
	}else print "#";
	echo $row['id'].";".$row['title'].";".$row['content'].";".$row['time'];
}
// if connection failed, echo message

mysqli_close($link);


?>

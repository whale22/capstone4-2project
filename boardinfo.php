<?php
$title=$_POST['title'];
$link = mysqli_connect("localhost","root","1111","test");
mysqli_query($link,"SET NAMES utf8");
if(!$link){
	die('Could not connect:'.mysql_error());
}
// if connection failed, echo message
if(strpos($title,"*&DeleteThis")!=false){
	$strTok=explode('*&',$title);
	print $strTok[0];
	$sql="delete from board_info where title='".$strTok[0]."';";
	mysqli_query($link,$sql);
	return;
}

$result=mysqli_query($link,"Select * from board_info where title='".$title."';");
while($row=mysqli_fetch_array($result)){
	echo $row['time']."&".$row['content']."&".$row['id'];
}

$result=mysqli_query($link,"SELECT * FROM board_comment WHERE title='".$title."';");
// if connection failed, echo message
$flag=false;
if(mysqli_fetch_array($result)) print("%");
$result=mysqli_query($link,"SELECT * FROM board_comment WHERE title='".$title."';");
while($row=mysqli_fetch_array($result)){
	if($flag==false) $flag=true;
	else print("#");
	echo $row['id'].";".$row['comment'].";".$row['time'];
}
mysqli_close($link);


?>

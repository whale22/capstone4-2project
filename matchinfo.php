<?php
$id=$_POST['id'];
$link = mysqli_connect("localhost","root","1111","test");
if(!$link){
	die('Could not connect:'.mysql_error());
}
mysqli_query($link,"SET NAMES utf8");
// if connection failed, echo message

$sql="SELECT * FROM tempuser WHERE Id='".$id."';";
$result=mysqli_query($link,$sql);
$row=mysqli_fetch_array($result);
print $row['User_Name'].":";
$sql="SELECT * FROM dog WHERE User_id='".$id."';";
$result=mysqli_query($link,$sql);
$flag=false;
while($row=mysqli_fetch_array($result)){
	if($flag==false){
		$flag=true;
	}else print ":";
	echo $row['Dog_Name'].":".$row['Age'].":".$row['Sex'].":".$row['Type'].":".$row['Weight'].":".$row['Size'].":".$row['dChar'];
}
// if connection failed, echo message

mysqli_close($link);


?>

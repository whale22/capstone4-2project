<?php
$id=$_POST['id'];
$pass=$_POST['pass'];
$name=$_POST['name'];
$add=$_POST['address'];
$docn=$_POST['dog_name'];
$dage=$_POST['dog_age'];
$dsex=$_POST['dog_sex'];
$type=$_POST['dog_type'];
$wei=$_POST['dog_weight'];
$size=$_POST['dog_size'];
$char=$_POST['dog_character'];
$link = mysqli_connect("localhost","root","1111","test");
mysqli_query($link,"SET NAMES utf8");
if(!$link){
	die('Could not connect:'.mysql_error());
}
// if connection failed, echo message
$sql="INSERT INTO tempuser (Id, Password, User_Name, Address) VALUES ('".$id."', '".$pass."', '".$name."', '".$add."');";
$ok=mysqli_query($link,$sql);

$sql="INSERT INTO dog (Dog_name, User_Id, Age, Sex, Type, Weight, Size, dChar) VALUES ('".$docn."', '".$id."', '".$dage."', '".$dsex."', '".$type."', '".$wei."', '".$size."', '".$char."');";
$ok=mysqli_query($link,$sql);

if($ok==false) print("false");
else print(json_encode("true"));
// if connection failed, echo message

mysqli_close($link);


?>
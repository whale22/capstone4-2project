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
if(!$link){
	die('Could not connect:'.mysql_error());
}
mysqli_query($link,"SET NAMES utf8");
// if connection failed, echo message

$sql="UPDATE tempuser SET Password='".$pass."', User_Name='".$name."', Address='".$add."' WHERE id='".$id."';";
$result=mysqli_query($link,$sql);
$sql="UPDATE dog SET Dog_name='".$docn."', Age='".$dage."', Sex='".$dsex."', Type='".$type."', Weight=".$wei.", Size='".$size."', dChar='".$char."' WHERE User_Id='".$id."';";
$result=mysqli_query($link,$sql);
if($result==false) print("false");
else print("true");

mysqli_close($link);


?>

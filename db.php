<?php
	$mysql = new mysqli(

	"localhost",
	"root",
	"",
	"android"

	);

if ($mysql->connect_error){
	die("failed to connect" . $mysql->connect_error);
}
<?php

	if ($_SERVER['REQUEST_METHOD'] == 'POST') {
		
		require_once("db.php");

		$id = $_POST['id'];
		$usuario = $_POST['usuario'];
		$monedas = $_POST['monedas'];

		$query = "INSERT INTO users (id, usuario, monedas) VALUES ('$id', '$usuario', '$monedas')";
		$result = $mysql->query($query);

		if ($result === TRUE) {

			echo "the user was created successfully";
		
		}else{
			echo "error";
		}

		$mysql->close();

	}

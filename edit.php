<?php

	if ($_SERVER['REQUEST_METHOD'] == 'POST') {
		
		require_once("db.php");

		$id = $_POST['id'];
		$monedas = $_POST['monedas'];

		$query = "UPDATE users SET monedas = '$monedas' WHERE id = '$id'";
		$result = $mysql->query($query);

		if ($mysql->affected_rows > 0) {
			if ($result === TRUE) {
				echo "update successfully";
			}else {
				echo "error";
			}

			
		}else{
			echo "not found any rows";
		}


		$mysql->close();

	}

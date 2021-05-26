<?php
	$f = htmlspecialchars($_REQUEST['fname']);
	$dir = "./uploads/".$f;
	unlink($dir);
?>

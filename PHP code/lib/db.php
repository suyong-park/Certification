<?php

function db_init($dbuser, $dbpass, $dbsid) {
	$conn = @oci_connect($dbuser, $dbpass, $dbsid, 'AL32UTF8');
	return $conn;
	}



?>

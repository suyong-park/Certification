<?php 
	header("Content-Type:text/html;charset=utf-8");
	require("../config/config.php");
	require("../lib/db.php");

	$conn = db_init($config["dbuser"], $config["dbpass"], $config["dbsid"]);
	
	$query = 'select NAME, CATEGORY from certification';

	$stmt = oci_parse($conn, $query);
	oci_execute($stmt);
	$result = array();

	while($row = oci_fetch_array($stmt))
	{
		array_push($result, array("NAME"=>$row[0], "CATEGORY"=>$row[1]));
	}
	echo json_encode($result, JSON_UNESCAPED_UNICODE);

	oci_free_statement($stmt);
	oci_close($conn);
?>

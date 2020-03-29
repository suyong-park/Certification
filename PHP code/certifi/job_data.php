<?php

        # 안드로이드 화면에서 각직업에 대한 내용이 열거되는 부분
        header("Content-Type:text/html;charset=utf-8");
        require("../config/config.php");
        require("../lib/db.php");

        $conn = db_init($config["dbuser"], $config["dbpass"], $config["dbsid"]);

	$query = 'SELECT NAME, CATEGORY, DESCRIPTION, LINK, CERTIFICATION, LISTAGG(NUM, \',\') WITHIN GROUP (ORDER BY NUM) AS NUM FROM (select j.name, j.category, j.description, j.link, j.certification, c.num from job j, certification c where j.category = c.job_category) group by name, category, description, link, certification';

        $stmt = oci_parse($conn, $query);
        oci_execute($stmt);
	$result = array();

	while($row = oci_fetch_array($stmt)) {
		array_push($result, array("NAME"=>$row[0],
					"CATEGORY"=>$row[1],
					"DESCRIPTION"=>$row[2],
					"LINK"=>$row[3],
					"CERTIFICATION_NAME"=>$row[4]));
	}

	echo json_encode($result, JSON_UNESCAPED_UNICODE);

        oci_free_statement($stmt);
        oci_close($conn);
?>

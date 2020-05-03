<?php

	# 안드로이드 화면에서 각 자격증에 대한 내용이 열거되는 부분
	header("Content-Type:text/html;charset=utf-8");
        require("../config/config.php");
        require("../lib/db.php");

	$conn = db_init($config["dbuser"], $config["dbpass"], $config["dbsid"]);

	$query = 'select * from certification, test_date where certification.NAME = test_date.NAME';

        $stmt = oci_parse($conn, $query);
	oci_execute($stmt);
	$result = array();
	while($row = oci_fetch_array($stmt)) {
		array_push($result, array("NAME"=>$row[0],
                                        "DESCRIPTION"=>$row[1],
                                        "COMPANY"=>$row[2],
                                        "JOB"=>$row[3],
                                        "LINK"=>$row[4],
                                        "NUM"=>$row[6],
					"SUBJECT_WRITTEN"=>$row[8],
					"SUBJECT_PRACTICAL"=>$row[9],
					
                                        "RECEIPT_DATE"=>$row[10],
                                        "WRITTEN_DATE"=>$row[11],
					"PRACTICAL_DATE"=>$row[12],
					"ANNOUNCEMENT_DATE"=>$row[13]));
	
	}

	echo json_encode($result, JSON_UNESCAPED_UNICODE);
	oci_free_statement($stmt);
	oci_close($conn);
?>

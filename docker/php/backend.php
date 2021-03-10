<?php

// Method: POST, GET etc
// Data: array("param" => "value") ==> index.php?param=value

function CallJSONAPI($method, $url, $data = false)
{
	$curl = curl_init();

	switch ($method)
	{

		case "GET":
			curl_setopt($curl, CURLOPT_HTTPGET, 1);
			break;

		case "POST":
			curl_setopt($curl, CURLOPT_POST, 1);
			if ($data)
				curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
			break;

		case "PUT":
			curl_setopt($curl, CURLOPT_CUSTOMREQUEST, 'PUT');
			if ($data)
				curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
			break;

		case "DELETE":
			curl_setopt($curl, CURLOPT_CUSTOMREQUEST, 'DELETE');
			break;

		default:
			if ($data)
				$url = sprintf("%s?%s", $url, http_build_query($data));
			

	}

	curl_setopt($curl, CURLOPT_URL, $url);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);

	$result = curl_exec($curl);
	$contentType = curl_getinfo($curl, CURLINFO_CONTENT_TYPE);
	$status_code = curl_getinfo($curl, CURLINFO_HTTP_CODE);
	header("Content-Type: ".$contentType);
	http_response_code($status_code);
	echo $result;
	curl_close($curl);
	return $result;
}

$base = "http://backend.mood-net:8080";
CallJSONAPI($_SERVER['REQUEST_METHOD'], $base.$_REQUEST['target'], file_get_contents('php://input'));

?>

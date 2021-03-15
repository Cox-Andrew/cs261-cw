<?php

// Method: POST, GET etc
// Data: array("param" => "value") ==> index.php?param=value

function CallJSONAPI($method, $url, $data = false)
{
    $curl = curl_init();

    switch ($method)
    {
        case "POST":
            curl_setopt($curl, CURLOPT_POST, 1);

            if ($data)
                curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
            break;
        case "GET":
        default:
            if ($data)
                $url = sprintf("%s?%s", $url, http_build_query($data));
    }

    curl_setopt($curl, CURLOPT_URL, $url);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);

    $result = curl_exec($curl);
    echo $result;
    curl_close($curl);
    return $result;
}

header("Content-Type: application/json");
CallJSONAPI("GET","http://backend.mood-net:8080/v0/forms?hostID=0",false);

?>

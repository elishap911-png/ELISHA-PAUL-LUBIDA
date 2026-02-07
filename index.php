<?php
error_reporting(E_ALL);
ini_set("display_errors",1);

$module = $_GET['module'] ?? '';

if($module=='account'){
    require __DIR__."/http/account.http.php";
}
elseif($module=='event'){
    require __DIR__."/http/event.http.php";
}
else{
    echo json_encode([
        "message"=>"Library Management Backend API Running"
    ]);
}

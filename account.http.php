<?php
require_once __DIR__."/../engine/db.engine.php";
require_once __DIR__."/../services/account.service.php";

$db = DBEngine::open();
$action = $_GET['action'] ?? '';

if($action=='register'){
    echo AccountService::register(
        $db,
        $_GET['name'],
        $_GET['email'],
        $_GET['password'],
        $_GET['role']
    );
}

if($action=='login'){
    echo AccountService::login(
        $db,
        $_GET['email'],
        $_GET['password']
    );
}

if($action=='logout'){
    AccountService::logout();
}

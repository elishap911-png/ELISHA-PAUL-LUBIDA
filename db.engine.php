<?php
class DBEngine {
    public static function open(){
        session_start();
        $db = new mysqli("localhost","root","","library_management_db");
        if($db->connect_error){
            die(json_encode(["error"=>"Database connection error"]));
        }
        return $db;
    }
}

<?php
class AccountService {

    public static function register($db,$name,$email,$pass,$role){
        $check = $db->query("SELECT id FROM users WHERE email='$email'");
        if($check->num_rows > 0){
            return json_encode(["error"=>"Account already exists"]);
        }

        $hash = password_hash($pass,PASSWORD_DEFAULT);
        $db->query(
            "INSERT INTO users(fullname,email,password,role)
             VALUES('$name','$email','$hash','$role')"
        );
        return json_encode(["message"=>"Registration successful"]);
    }

    public static function login($db,$email,$pass){
        $res = $db->query("SELECT * FROM users WHERE email='$email'");
        if($res->num_rows === 1){
            $u = $res->fetch_assoc();
            if(password_verify($pass,$u['password'])){
                $_SESSION['uid']  = $u['id'];
                $_SESSION['role'] = $u['role'];
                return json_encode(["message"=>"Login successful"]);
            }
        }
        return json_encode(["error"=>"Invalid login"]);
    }

    public static function auth(){
        if(!isset($_SESSION['uid'])){
            die(json_encode(["error"=>"Login required"]));
        }
    }

    public static function logout(){
        session_destroy();
        echo json_encode(["message"=>"Logged out"]);
    }
}

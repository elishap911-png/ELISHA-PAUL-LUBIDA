<?php
require_once __DIR__."/../engine/db.engine.php";
require_once __DIR__."/../services/account.service.php";
require_once __DIR__."/../services/event.service.php";

$db = DBEngine::open();
AccountService::auth();

$uid  = $_SESSION['uid'];
$role = $_SESSION['role'];
$action = $_GET['action'] ?? '';

/* Create Event */
if($action=='create'){
    if($role!='organizer') die(json_encode(["error"=>"Organizers only"]));
    echo EventService::createEvent(
        $db,
        $_GET['title'],
        $_GET['location'],
        $_GET['date'],
        $uid
    )
    ? json_encode(["message"=>"Event created"])
    : json_encode(["error"=>"Failed"]);
}

/* List Events */
if($action=='list'){
    EventService::listEvents($db);
}

/* Update Event */
if($action=='update'){
    if($role!='organizer') die(json_encode(["error"=>"Organizers only"]));
    echo EventService::updateEvent(
        $db,
        $_GET['event_id'],
        $_GET['title'],
        $_GET['location'],
        $_GET['date']
    )
    ? json_encode(["message"=>"Event updated"])
    : json_encode(["error"=>"Update failed"]);
}

/* Delete Event */
if($action=='delete'){
    if($role!='organizer') die(json_encode(["error"=>"Organizers only"]));
    echo EventService::deleteEvent($db,$_GET['event_id'])
        ? json_encode(["message"=>"Event deleted"])
        : json_encode(["error"=>"Delete failed"]);
}

/* Join Event */
if($action=='join'){
    if($role!='member') die(json_encode(["error"=>"Members only"]));
    echo EventService::joinEvent($db,$_GET['event_id'],$uid)
        ? json_encode(["message"=>"Joined event"])
        : json_encode(["error"=>"Failed"]);
}

/* Remove Participant */
if($action=='remove-participant'){
    if($role!='organizer') die(json_encode(["error"=>"Organizers only"]));
    echo EventService::removeParticipant(
        $db,
        $_GET['event_id'],
        $_GET['member_id']
    )
    ? json_encode(["message"=>"Participant removed"])
    : json_encode(["error"=>"Failed"]);
}

/* List Participants */
if($action=='participants'){
    EventService::participants($db);
}

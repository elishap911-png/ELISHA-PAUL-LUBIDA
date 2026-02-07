<?php
class EventService {

    public static function createEvent($db,$title,$location,$date,$org){
        return $db->query(
            "INSERT INTO events(title,location,event_date,organizer_id)
             VALUES('$title','$location','$date',$org)"
        );
    }

    public static function listEvents($db){
        $res = $db->query(
            "SELECT e.id,e.title,e.location,e.event_date,u.fullname organizer
             FROM events e
             JOIN users u ON e.organizer_id=u.id"
        );
        $data=[];
        while($r=$res->fetch_assoc()) $data[]=$r;
        echo json_encode($data);
    }

    public static function updateEvent($db,$id,$title,$location,$date){
        return $db->query(
            "UPDATE events 
             SET title='$title', location='$location', event_date='$date'
             WHERE id=$id"
        );
    }

    public static function deleteEvent($db,$id){
        return $db->query("DELETE FROM events WHERE id=$id");
    }

    public static function joinEvent($db,$event,$member){
        return $db->query(
            "INSERT INTO event_participants(event_id,member_id)
             VALUES($event,$member)"
        );
    }

    public static function removeParticipant($db,$event_id,$member_id){
        return $db->query(
            "DELETE FROM event_participants
             WHERE event_id=$event_id AND member_id=$member_id"
        );
    }

    public static function participants($db){
        $res = $db->query(
            "SELECT u.fullname member,e.title event,p.joined_at
             FROM event_participants p
             JOIN users u ON p.member_id=u.id
             JOIN events e ON p.event_id=e.id"
        );
        $data=[];
        while($r=$res->fetch_assoc()) $data[]=$r;
        echo json_encode($data);
    }
}

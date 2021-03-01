namespace java com.altran.notification.thrift.impl

 struct Slot {
    1: i32  sid,
    2: i32 startTime,
    3: i32 endTime
 }

 struct ReservationNotification {
    1: i32 rid,
    2: string emailId,
    3: i32 checkInTime,
    4: i32 checkOutTime,
    5: string city,
    6: bool confirmed,
    7: list<Slot> slotList
 }

service NotificationService {

    ReservationNotification get() ,

    bool save(1:ReservationNotification reservationNotification) ,

    bool status()
}


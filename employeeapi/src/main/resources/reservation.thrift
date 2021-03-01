namespace java com.altran.reservation

struct Reservation {
    1: string emailId,
    2: i32 checkInTime,
    3: i32 checkOutTime,
    4: string city
}

service ReservationService {

    Reservation get() ,

    bool save(1:Reservation reservation) ,

    bool status()
}


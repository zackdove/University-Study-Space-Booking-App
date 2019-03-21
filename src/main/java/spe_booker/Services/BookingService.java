package spe_booker.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import spe_booker.Repositorys.BookingRepository;
import spe_booker.Repositorys.UserRepository;
import spe_booker.models.Booking;
import spe_booker.models.Room;
import spe_booker.models.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class BookingService {


    private BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }


    public Booking createBooking(Long length, Date date, User user, Room room) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date creationDate = new Date();

        System.out.print("Booking Date: "+date+"\n");

        Booking b = new Booking();
        b.setLength(length);
        b.setDateTime(date);
        b.setUser(user);
        b.setRoom(room);
        b.setCreationDate(creationDate);
        saveBooking(b);
        return b;
    }

    public Booking saveBooking(Booking booking) {

        return bookingRepository.save(booking);

    }

}

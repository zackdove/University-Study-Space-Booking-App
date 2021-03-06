package spe_booker.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spe_booker.Services.BookingService;
import spe_booker.Services.RoomService;
import spe_booker.Services.UserService;
import spe_booker.models.Booking;
import spe_booker.models.BookingRequest;
import spe_booker.models.Room;
import spe_booker.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes("bookingRequest")
public class BookingController {
    private static final Logger LOG = LoggerFactory.getLogger(BookingController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingService bookingService;

    @GetMapping(value = {"/bookings"})
    public String viewBookings(Model model) {
        LOG.info("Listing bookings for viewbookings");
        List<Booking> bookings = new ArrayList<>();
        if (userService.getCurrentUser().get().isAdmin()){
            bookings.addAll(bookingService.findAllFutureBookings());
        } else {
            bookings.addAll(bookingService.findFutureBookingsForUser(userService.getCurrentUser().get()));
        }
        model.addAttribute("bookings", bookings);
        return "booking_view_list";
    }

    @GetMapping(value = {"/user/{username}/bookings"})
    public String viewBookingsForuser(@PathVariable String username, Model model) {
        LOG.info("Listing bookings for a specific user\n");
        List<Booking> currentUserBookings = bookingService.findBookingsByUser(userService.findByUsername(username).get());
        model.addAttribute("bookings", currentUserBookings);
        return "booking_view_list";
    }

     Date roundToNextHour(Date datetime){
        long resultLong = Math.round((datetime.getTime() / 3600000) + 0.5) * 3600000;
        Date resultDate = new Date();
        resultDate.setTime(resultLong);
        return resultDate;

    }

    @GetMapping("/booking/add")
    public String makeBooking(Model model) {
        BookingRequest bookingRequest = new BookingRequest();
        Date currentDateTime = new Date();
        Date nextHour = roundToNextHour(currentDateTime);
        bookingRequest.setStartDateTime(nextHour);
        bookingRequest.setDuration((long) 2);
        model.addAttribute("bookingRequest", bookingRequest);
        return "booking_add";
    }

    private Boolean isDateInPast(Date date){
        Date currentDate = new Date();
        return (date.getTime() < currentDate.getTime());
    }

    private Boolean isDateMoreThanTwoWeeksAway(Date date){
        Date currentDate = new Date();
        return (date.getTime() > (currentDate.getTime() + 1209600000 ));
    }

    private Boolean isMoreThan4HoursBookedToday(BookingRequest bookingRequest){
        User user = userService.getCurrentUser().get();
        List<Booking> bookings = user.getBookings();
        Date requestStartTime = bookingRequest.getStartDateTime();
        Long numberOfHoursWithin24Hours = (long) 0;
        for (Booking booking : bookings){
            Date startDateTime = booking.getStartDateTime();
            if ( (startDateTime.getTime() > requestStartTime.getTime() - 43200000) && (startDateTime.getTime() < (43200000 + requestStartTime.getTime()))){
                numberOfHoursWithin24Hours += booking.getDuration();
            }
        }
        numberOfHoursWithin24Hours += bookingRequest.getDuration();
        System.out.print("###### Hours " + numberOfHoursWithin24Hours + "\n");
        if (numberOfHoursWithin24Hours > 4){
            LOG.info("User has requested more than 4 booking hours within a day");
            return true;
        }
        else {
            return false;
        }
    }

    @PostMapping("/booking/add")
    public String submitDateTime(@ModelAttribute BookingRequest bookingRequest, RedirectAttributes redirectAttributes) {
        Boolean dateInPast = isDateInPast(bookingRequest.getStartDateTime());
        Boolean dateMoreThanTwoWeeksAway = isDateMoreThanTwoWeeksAway(bookingRequest.getStartDateTime());
        Boolean moreThan4HoursBookedToday = isMoreThan4HoursBookedToday(bookingRequest);
        //Admins have no limit on hours
        if (userService.getCurrentUser().get().isAdmin()) {moreThan4HoursBookedToday = false;}
        Long duration = bookingRequest.getDuration();
        Boolean durationNotValid = ( duration > 3 || duration <= 0 );
        if (dateInPast || dateMoreThanTwoWeeksAway || durationNotValid || moreThan4HoursBookedToday){
            redirectAttributes.addFlashAttribute("dateInPast", dateInPast);
            redirectAttributes.addFlashAttribute("dateMoreThanTwoWeeksAway", dateMoreThanTwoWeeksAway);
            redirectAttributes.addFlashAttribute("durationNotValid", durationNotValid);
            redirectAttributes.addFlashAttribute("moreThan4HoursBookedToday", moreThan4HoursBookedToday);
            return "redirect:/booking/add";
        } else {
            redirectAttributes.addFlashAttribute("bookingRequest", bookingRequest);
            return "redirect:/booking/add/room";
        }
    }

    @GetMapping(value = {"/booking/add/room"})
    public String makebookingRoom(@ModelAttribute("bookingRequest") BookingRequest bookingRequest, Model model) {
        model.addAttribute("bookingRequestDateAndDuration", bookingRequest);
        model.addAttribute("rooms", roomService.findAvailable(bookingRequest.getStartDateTime(), bookingRequest.getEndDateTime()));
        return "booking_add_room";
    }

    @PostMapping("/booking/add/room/{building}/{roomNo}")
    public String submitBookingRoom(@ModelAttribute("bookingRequest") BookingRequest bookingRequest, @PathVariable String building, @PathVariable String roomNo, Model model){
        bookingRequest.setBuilding(building);
        bookingRequest.setRoomNo(roomNo);
        User user = userService.getCurrentUser().get();
        if (user.getBlacklisted()){
            LOG.info("Blacklisted user attempted to make booking, but was blocked.");
            return "/error/error";
        } else {
            Optional<Room> room = roomService.findByRoomNoAndBuilding(roomNo, building);
            if (room.isPresent()){
                Booking booking1 = bookingService.createBookingFromBookingRequest(bookingRequest, user, room.get());
                return "booking_confirmed";
            } else {
                LOG.info("Room not found for booking creation.\n");
                return "/error/error-400";
            }
        }
    }

    @PostMapping(value = {"/booking/delete/{id}"})
    public String deleteBooking(@PathVariable Long id){
        LOG.info("Deleting booking: "+ id+ "\n");
        Optional<Booking> booking = bookingService.findById(id);
        if (booking.isPresent()){
            bookingService.deleteBooking(booking.get());
            return "booking_cancelled";
        } else {
            LOG.info("#Booking not present!");
            return "/error/error";
        }
    }

    //Called automatically when building the model
    //Formats the date/time
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"), true));
    }



}

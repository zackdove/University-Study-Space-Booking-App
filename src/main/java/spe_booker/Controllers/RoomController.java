package spe_booker.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import spe_booker.Services.RoomService;
import spe_booker.models.Room;

@Controller
public class RoomController {

    private static final Logger LOG = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private RoomService roomService;

    @GetMapping("/room/add")
    public String addRoom(Model model) {
        model.addAttribute("room", new Room());
        return "room_add";
    }

    @PostMapping("/room/add")
    public String submitRoom(@ModelAttribute Room room) {
        LOG.info("Saving new room with room number " + room.getRoomNo());
        Room room1 = roomService.save(room);
        return "room_added";
    }

    @GetMapping(value = {"/rooms"})
    public String viewRooms(Model model) {
        LOG.info("Listing rooms");
        model.addAttribute("rooms", roomService.findAll());
        return "room_view_list";
    }
    
    @PostMapping(value = {"/room/delete/{id}"})
    public String deleteUser(@PathVariable Long id){
        LOG.info("Deleting room with id " + id);
        roomService.deleteById(id);
        return "room_deleted";

    }

}

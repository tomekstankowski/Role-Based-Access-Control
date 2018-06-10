package com.stankowski_strzelka.rbac.controller;

import com.stankowski_strzelka.rbac.dto.AppointmentDto;
import com.stankowski_strzelka.rbac.model.Appointment;
import com.stankowski_strzelka.rbac.service.AppointmentService;
import com.stankowski_strzelka.rbac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("appointments")
public class AppointmentController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;


    @ModelAttribute("addAppointment")
    public AppointmentDto appointmentModel() {
        return new AppointmentDto();
    }

    @GetMapping("/list")
    public String userApp(Model model) {
        List<Appointment> appointments = appointmentService.getAllPatientAppointments(
                userService.getCurrentUser()
        );
        List<Appointment> availableAppointments = appointmentService.getAllPossibleAppointments();

        model.addAttribute("appointments", appointments);
        model.addAttribute("availableAppointments", availableAppointments);

        return "user/appointments";
    }

    @PostMapping("/add")
    public String addAppointment(@ModelAttribute("addAppointment") AppointmentDto appointment) throws Exception {
        appointmentService.saveAppointment(appointment);
        return "redirect:/appointments/list?added";
    }

    @PostMapping("/{id}/delete")
    public String deleteDuty(@PathVariable long id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/appointments/list?deleted";
    }
}

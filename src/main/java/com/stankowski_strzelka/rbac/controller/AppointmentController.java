package com.stankowski_strzelka.rbac.controller;

import com.stankowski_strzelka.rbac.dto.AppointmentDto;
import com.stankowski_strzelka.rbac.model.Appointment;
import com.stankowski_strzelka.rbac.service.AppointmentService;
import com.stankowski_strzelka.rbac.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final UserService userService;
    private final AppointmentService appointmentService;

    @ModelAttribute("addAppointment")
    public AppointmentDto appointmentModel() {
        return new AppointmentDto();
    }

    @GetMapping("/list")
    @PreAuthorize(("@securityService.hasPrivilege('READ_APPOINTMENTS')"))
    public String userApp(Model model) {

        List<Appointment> appointments = appointmentService.getAllPatientAppointments(
                userService.getCurrentUser()
        );
        List<Appointment> availableAppointments = appointmentService.getAllPossibleAppointments();

        model.addAttribute("appointments", appointments);
        model.addAttribute("availableAppointments", availableAppointments);

        return "user/appointments";
    }

    @GetMapping("medical/list")
    @PreAuthorize(("@securityService.hasPrivilege('READ_MEDICAL_APPOINTMENTS')"))
    public String medicalApp(Model model) {

        List<Appointment> appointments = appointmentService.getAllMedicalAppointments(
                userService.getCurrentUser()
        );

        model.addAttribute("appointments", appointments);
        return "user/medical_appointments";
    }

    @PostMapping("/add")
    @PreAuthorize(("@securityService.hasPrivilege('CREATE_APPOINTMENTS')"))
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

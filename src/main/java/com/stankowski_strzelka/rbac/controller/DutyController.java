package com.stankowski_strzelka.rbac.controller;

import com.stankowski_strzelka.rbac.dto.DutyCreationDto;
import com.stankowski_strzelka.rbac.dto.DutyDto;
import com.stankowski_strzelka.rbac.exception.ConflictException;
import com.stankowski_strzelka.rbac.model.User;
import com.stankowski_strzelka.rbac.service.DutyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user/duties")
@RequiredArgsConstructor
public class DutyController {
    private final DutyService dutyService;
    private final ModelMapper modelMapper;

    @ModelAttribute("duty")
    public DutyCreationDto dutyAttribute() {
        return new DutyCreationDto();
    }

    @ModelAttribute("duties")
    public List<DutyDto> dutiesAttribute(HttpSession session) {
        final User medical = (User) session.getAttribute("user");
        return dutyService.getDuties(medical)
                .stream()
                .map(entity -> modelMapper.map(entity, DutyDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping
    @PreAuthorize(("hasAuthority('READ_DUTIES')"))
    public String userDuties() {
        return "user/duties";
    }

    @PostMapping
    @PreAuthorize(("hasAuthority('CREATE_DUTIES')"))
    public String createDuty(@Valid @ModelAttribute("duty") DutyCreationDto duty,
                             BindingResult bindingResult,
                             HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "/user/duties";
        }
        final User medical = (User) session.getAttribute("user");
        try {
            dutyService.createDuty(duty, medical);
        } catch (ConflictException ex) {
            bindingResult.reject("dd", ex.getMessage());
            return "/user/duties";
        }
        return "redirect:/user/duties?added";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize(("hasAuthority('DELETE_DUTIES')"))
    public String deleteDuty(@PathVariable long id) {
        dutyService.deleteDuty(id);
        return "redirect:/user/duties?deleted";
    }
}

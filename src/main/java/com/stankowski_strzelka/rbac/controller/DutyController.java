package com.stankowski_strzelka.rbac.controller;

import com.stankowski_strzelka.rbac.dto.DutyCreationDto;
import com.stankowski_strzelka.rbac.dto.DutyDto;
import com.stankowski_strzelka.rbac.exception.BadRequestException;
import com.stankowski_strzelka.rbac.exception.ConflictException;
import com.stankowski_strzelka.rbac.exception.ResourceNotFoundException;
import com.stankowski_strzelka.rbac.model.User;
import com.stankowski_strzelka.rbac.service.DutyService;
import com.stankowski_strzelka.rbac.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user/{id}/duties")
@RequiredArgsConstructor
public class DutyController {
    private final DutyService dutyService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @ModelAttribute("duty")
    public DutyCreationDto dutyAttribute() {
        return new DutyCreationDto();
    }

    @ModelAttribute("duties")
    public List<DutyDto> dutiesAttribute(@PathVariable long id, Model model) {
        final User medical = userService.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("User with id %d could not be found", id)));
        model.addAttribute("email", medical.getEmail());
        model.addAttribute("medicalId", id);
        model.addAttribute("medical", medical);
        return dutyService.getDuties(medical)
                .stream()
                .map(entity -> modelMapper.map(entity, DutyDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping
    @PreAuthorize(("@securityService.hasPrivilege('READ_DUTIES') AND principal.username == #modelMap.get('email')"))
    public String getDutiesView(ModelMap modelMap) {
        return "user/duties";
    }

    @PostMapping
    @PreAuthorize(("@securityService.hasPrivilege('CREATE_DUTIES') AND principal.username == #modelMap.get('email')"))
    public String createDuty(@PathVariable long id,
                             @Valid @ModelAttribute("duty") DutyCreationDto duty,
                             BindingResult bindingResult,
                             ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            return "/user/duties";
        }
        final User medical = (User) modelMap.get("medical");
        try {
            dutyService.createDuty(duty, medical);
        } catch (ConflictException ex) {
            bindingResult.reject("conflict", ex.getMessage());
            return "/user/duties";
        }
        return String.format("redirect:/user/%d/duties?added", id);
    }

    @PostMapping("/{dutyId}")
    @PreAuthorize(("@securityService.hasPrivilege('DELETE_DUTIES') AND principal.username == #modelMap.get('email')"))
    public String deleteDuty(@PathVariable long id,
                             @PathVariable long dutyId,
                             @RequestParam String action,
                             ModelMap modelMap) {
        if (action.equals("delete")) {
            dutyService.deleteDuty(dutyId);
            return String.format("redirect:/user/%d/duties?deleted", id);
        }
        throw new BadRequestException("Unexpected 'action' parameter value");
    }
}

package io.bootify.practica_spring_batch.controller;

import io.bootify.practica_spring_batch.model.ControlLoteDTO;
import io.bootify.practica_spring_batch.model.Estado;
import io.bootify.practica_spring_batch.service.ControlLoteService;
import io.bootify.practica_spring_batch.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/controlLotes")
public class ControlLoteController {

    private final ControlLoteService controlLoteService;

    public ControlLoteController(final ControlLoteService controlLoteService) {
        this.controlLoteService = controlLoteService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("estadoValues", Estado.values());
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("controlLotes", controlLoteService.findAll());
        return "controlLote/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("controlLote") final ControlLoteDTO controlLoteDTO) {
        return "controlLote/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("controlLote") @Valid final ControlLoteDTO controlLoteDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "controlLote/add";
        }
        controlLoteService.create(controlLoteDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("controlLote.create.success"));
        return "redirect:/controlLotes";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("controlLote", controlLoteService.get(id));
        return "controlLote/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("controlLote") @Valid final ControlLoteDTO controlLoteDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "controlLote/edit";
        }
        controlLoteService.update(id, controlLoteDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("controlLote.update.success"));
        return "redirect:/controlLotes";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = controlLoteService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            controlLoteService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("controlLote.delete.success"));
        }
        return "redirect:/controlLotes";
    }

}

package io.bootify.practica_spring_batch.controller;

import io.bootify.practica_spring_batch.domain.ControlLote;
import io.bootify.practica_spring_batch.model.AuditoriaTransaccionDTO;
import io.bootify.practica_spring_batch.model.Estado;
import io.bootify.practica_spring_batch.repos.ControlLoteRepository;
import io.bootify.practica_spring_batch.service.AuditoriaTransaccionService;
import io.bootify.practica_spring_batch.util.CustomCollectors;
import io.bootify.practica_spring_batch.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/auditoriaTransaccions")
public class AuditoriaTransaccionController {

    private final AuditoriaTransaccionService auditoriaTransaccionService;
    private final ControlLoteRepository controlLoteRepository;

    public AuditoriaTransaccionController(
            final AuditoriaTransaccionService auditoriaTransaccionService,
            final ControlLoteRepository controlLoteRepository) {
        this.auditoriaTransaccionService = auditoriaTransaccionService;
        this.controlLoteRepository = controlLoteRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("estadoTransaccionValues", Estado.values());
        model.addAttribute("controlLoteValues", controlLoteRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(ControlLote::getId, ControlLote::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("auditoriaTransaccions", auditoriaTransaccionService.findAll());
        return "auditoriaTransaccion/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("auditoriaTransaccion") final AuditoriaTransaccionDTO auditoriaTransaccionDTO) {
        return "auditoriaTransaccion/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("auditoriaTransaccion") @Valid final AuditoriaTransaccionDTO auditoriaTransaccionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auditoriaTransaccion/add";
        }
        auditoriaTransaccionService.create(auditoriaTransaccionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("auditoriaTransaccion.create.success"));
        return "redirect:/auditoriaTransaccions";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("auditoriaTransaccion", auditoriaTransaccionService.get(id));
        return "auditoriaTransaccion/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("auditoriaTransaccion") @Valid final AuditoriaTransaccionDTO auditoriaTransaccionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auditoriaTransaccion/edit";
        }
        auditoriaTransaccionService.update(id, auditoriaTransaccionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("auditoriaTransaccion.update.success"));
        return "redirect:/auditoriaTransaccions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        auditoriaTransaccionService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("auditoriaTransaccion.delete.success"));
        return "redirect:/auditoriaTransaccions";
    }

}

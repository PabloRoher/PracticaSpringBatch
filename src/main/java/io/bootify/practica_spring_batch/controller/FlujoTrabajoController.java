package io.bootify.practica_spring_batch.controller;

import io.bootify.practica_spring_batch.domain.ControlLote;
import io.bootify.practica_spring_batch.model.Estado;
import io.bootify.practica_spring_batch.model.FlujoTrabajoDTO;
import io.bootify.practica_spring_batch.repos.ControlLoteRepository;
import io.bootify.practica_spring_batch.service.FlujoTrabajoService;
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
@RequestMapping("/flujoTrabajos")
public class FlujoTrabajoController {

    private final FlujoTrabajoService flujoTrabajoService;
    private final ControlLoteRepository controlLoteRepository;

    public FlujoTrabajoController(final FlujoTrabajoService flujoTrabajoService,
            final ControlLoteRepository controlLoteRepository) {
        this.flujoTrabajoService = flujoTrabajoService;
        this.controlLoteRepository = controlLoteRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("estadoPasoValues", Estado.values());
        model.addAttribute("lotesValues", controlLoteRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(ControlLote::getId, ControlLote::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("flujoTrabajoes", flujoTrabajoService.findAll());
        return "flujoTrabajo/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("flujoTrabajo") final FlujoTrabajoDTO flujoTrabajoDTO) {
        return "flujoTrabajo/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("flujoTrabajo") @Valid final FlujoTrabajoDTO flujoTrabajoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "flujoTrabajo/add";
        }
        flujoTrabajoService.create(flujoTrabajoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("flujoTrabajo.create.success"));
        return "redirect:/flujoTrabajos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("flujoTrabajo", flujoTrabajoService.get(id));
        return "flujoTrabajo/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("flujoTrabajo") @Valid final FlujoTrabajoDTO flujoTrabajoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "flujoTrabajo/edit";
        }
        flujoTrabajoService.update(id, flujoTrabajoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("flujoTrabajo.update.success"));
        return "redirect:/flujoTrabajos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        flujoTrabajoService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("flujoTrabajo.delete.success"));
        return "redirect:/flujoTrabajos";
    }

}

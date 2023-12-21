package io.bootify.practica_spring_batch.controller;

import io.bootify.practica_spring_batch.model.CuentaDTO;
import io.bootify.practica_spring_batch.service.CuentaService;
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
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(final CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("cuentas", cuentaService.findAll());
        return "cuenta/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("cuenta") final CuentaDTO cuentaDTO) {
        return "cuenta/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("cuenta") @Valid final CuentaDTO cuentaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("numeroCuenta") && cuentaDTO.getNumeroCuenta() != null && cuentaService.numeroCuentaExists(cuentaDTO.getNumeroCuenta())) {
            bindingResult.rejectValue("numeroCuenta", "Exists.cuenta.numeroCuenta");
        }
        if (bindingResult.hasErrors()) {
            return "cuenta/add";
        }
        cuentaService.create(cuentaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("cuenta.create.success"));
        return "redirect:/cuentas";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("cuenta", cuentaService.get(id));
        return "cuenta/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("cuenta") @Valid final CuentaDTO cuentaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final CuentaDTO currentCuentaDTO = cuentaService.get(id);
        if (!bindingResult.hasFieldErrors("numeroCuenta") && cuentaDTO.getNumeroCuenta() != null &&
                !cuentaDTO.getNumeroCuenta().equalsIgnoreCase(currentCuentaDTO.getNumeroCuenta()) &&
                cuentaService.numeroCuentaExists(cuentaDTO.getNumeroCuenta())) {
            bindingResult.rejectValue("numeroCuenta", "Exists.cuenta.numeroCuenta");
        }
        if (bindingResult.hasErrors()) {
            return "cuenta/edit";
        }
        cuentaService.update(id, cuentaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("cuenta.update.success"));
        return "redirect:/cuentas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = cuentaService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            cuentaService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("cuenta.delete.success"));
        }
        return "redirect:/cuentas";
    }

}

package com.web.edutrade.controller;

//import ch.qos.logback.core.model.Model;
import com.web.edutrade.model.Customer;
import com.web.edutrade.model.PackageInfo;
import com.web.edutrade.model.Slide;
import com.web.edutrade.service.CustomerService;
import com.web.edutrade.service.PackageInfoService;
import com.web.edutrade.service.SlideService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/edutrade")
public class EdutradeController {

    @Autowired
    CustomerService customerService;

    @Autowired
    private PackageInfoService packageInfoService;

    @Autowired
    private SlideService slideService;

    @GetMapping
    public String index(Model model) {
        List<PackageInfo> packages = packageInfoService.getAllPackages();
        List<Slide> slides = slideService.getAllSlides();

        model.addAttribute("packages", packages);
        model.addAttribute("slides", slides);

        return "index";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("consultationForm", new Customer());
        return "signup";
    }

    @PostMapping("/api/register-consultation")
    public String processRegistration(
            @Valid @ModelAttribute("consultationForm") Customer customer,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        if (customer.getFlagProvision() == null || !customer.getFlagProvision()) {
            bindingResult.rejectValue("flagProvision", "error.flagProvision",
                    "Bạn cần đồng ý điều khoản để tiếp tục");
            return "signup";
        }

        try {
            customerService.save(customer);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Đăng ký thành công! Chúng tôi sẽ liên hệ bạn sớm nhất.");

            return "redirect:/edutrade";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra. Vui lòng thử lại sau.");
            return "signup";
        }
    }

}

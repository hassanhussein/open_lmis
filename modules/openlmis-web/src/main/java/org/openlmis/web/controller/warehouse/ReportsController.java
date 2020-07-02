package org.openlmis.web.controller.warehouse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/warehouse/reports")
public class ReportsController {
    @RequestMapping("/greeting")
    public String greeting() {
        System.out.println("called");
        return "greeting";
    }





}

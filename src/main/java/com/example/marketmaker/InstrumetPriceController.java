package com.example.marketmaker;

import com.example.marketmaker.dto.InstrumentPrice;
import com.example.marketmaker.dto.InstrumentQuotedPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class InstrumetPriceController {

    private QuoteCalculationEngine iPriceQuoteEngine = QuoteCalculationEngineImpl.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(InstrumetPriceController.class);
    private static final InstrumentQuotedPrice defaultPriceQuote = new InstrumentQuotedPrice();

    static {
        defaultPriceQuote.setSide("Buy");
    }

    @GetMapping("/")
    public String defaultForm(Model model) {
        logger.info("performing - default ");
        model.addAttribute("iPrice", defaultPriceQuote);
        return "instrumentprice";
    }

    @PostMapping("/instrumentprice")
    public String instrumentprice(@ModelAttribute("iPrice") InstrumentPrice price,
                                  BindingResult bindingResult,
                                  Model model) {

        logger.info("performing /instrumentprice");
        if (bindingResult.hasErrors()) {
            logger.warn(bindingResult.toString());
            return "error";
        }
        boolean isBuy = "buy".equalsIgnoreCase(price.getSide());
        double quotedPrice = iPriceQuoteEngine.calculateQuotePrice(price.getId(), 0, isBuy, price.getQuat());
        InstrumentQuotedPrice result = new InstrumentQuotedPrice();
        result.setId(price.getId());
        result.setSide(price.getSide());
        result.setQuat(price.getQuat());
        result.setQuotedPrice(quotedPrice);
        model.addAttribute("iPrice", result);
        return "instrumentprice";
    }

}

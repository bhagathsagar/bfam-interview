package com.example.marketmaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class QuoteCalculationEngineImpl implements QuoteCalculationEngine {

    /*
This solution make single instance of the QuotationCalculation, so making it a singleton
 */
    private static final QuoteCalculationEngine INSTANCE = new QuoteCalculationEngineImpl();
    private final ReferencePriceSource referencePriceSource = ReferencePriceSourceImpl.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(QuoteCalculationEngineImpl.class);

    private QuoteCalculationEngineImpl() {
    }


    public static QuoteCalculationEngine getInstance() {
        return INSTANCE;
    }

    @Override
    public double calculateQuotePrice(int securityId, double referencePrice, boolean buy, int quantity) {
        double price = referencePriceSource.get(securityId);
        return new BigDecimal(price).multiply(new BigDecimal(quantity)).doubleValue();
    }
}

package com.example.marketmaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MarketMakerMain {

    private static final Logger logger = LoggerFactory.getLogger(MarketMakerMain.class);

    public static void main(String[] args) {
        /*
            As problem state to use TCP, using HTTP implementation.
            Using spring-boot, Web server code.
            thymeleaf for the simple page using template.
         */
        SpringApplication.run(MarketMakerMain.class, args);


        /**
         *Also adding 2 price source listener just for sample run,
         */
        ReferencePriceSourceImpl.getInstance().subscribe((int instrumentId, double price) -> {
                    logger.info("LISTENER-001 Instrument price updated for id={} and price={}", instrumentId, price);
                }
        );
        ReferencePriceSourceImpl.getInstance().subscribe((int instrumentId, double price) -> {
                    logger.info("LISTENER-002 Instrument price updated for id={} and price={}", instrumentId, price);
                }
        );

    }

}

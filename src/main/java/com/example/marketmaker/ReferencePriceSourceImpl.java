package com.example.marketmaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class ReferencePriceSourceImpl implements ReferencePriceSource {

    /*
   This solution make single instance of the ReferncePriceSource, so making it a singleton
     */
    private static final ReferencePriceSourceImpl INSTANCE = new ReferencePriceSourceImpl();
    /*
        Prepare a set of Price change listener, using a concurrent implementation to make it thread safe
     */
    private final Set<ReferencePriceSourceListener> priceChangeListner = Collections.newSetFromMap(new ConcurrentHashMap<ReferencePriceSourceListener, Boolean>(0));

    /*
    Caching a instrument detail,using the concurrent hashMap to keep the price update in the cache as thread safe.
     */
    private final Map<Integer, Double> instrumentPrice = new ConcurrentHashMap();

    private final Logger logger = LoggerFactory.getLogger(ReferencePriceSourceImpl.class);

    private ReferencePriceSourceImpl() {
        Runnable myInstrumentPriceUpdater = () -> {
            //This is a dummy thread written just to prepare the stock price for runtime.
            //Assume the Instrument Id are ranged from 1-1000
            Random random = new Random();
            double diff = 0;
            boolean isNewPrice = true;
            while (true) {
                try {
                    int instrumentId = random.nextInt(1000);
                    Double currentPrice = instrumentPrice.get(instrumentId);
                    if (currentPrice == null) {
                        currentPrice = instrumentId * random.nextDouble();
                        logger.info("Preparing price for instrument = {} ", instrumentId);
                        isNewPrice = true;
                    } else {
                        isNewPrice = false;
                    }
                    diff = random.nextDouble();
                    currentPrice = random.nextBoolean() ? currentPrice + diff : currentPrice - diff;
                    updateInstrumentPrice(instrumentId, currentPrice);
                    if (!isNewPrice)
                        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
                } catch (Exception e) {
                }
            }
        };
        Thread thread = new Thread(myInstrumentPriceUpdater);
        thread.start();
    }

    public static ReferencePriceSource getInstance() {
        return INSTANCE;
    }

    @Override
    public void subscribe(ReferencePriceSourceListener listener) {
        priceChangeListner.add(listener);
    }

    @Override
    public double get(int securityId) {
        Double result = instrumentPrice.get(securityId);
        return result == null ? 0 : result;
    }

    private void updateInstrumentPrice(int instrumentId, Double newPrice) {
        Double previousPrice = instrumentPrice.put(instrumentId, newPrice);
        if (previousPrice != null && !previousPrice.equals(newPrice)) {
            priceChangeListner.forEach(l -> priceUpdater.submit(() -> {
                l.referencePriceChanged(instrumentId, newPrice);
            }));
        }
    }

    private final ExecutorService priceUpdater = Executors.newFixedThreadPool(1);
}

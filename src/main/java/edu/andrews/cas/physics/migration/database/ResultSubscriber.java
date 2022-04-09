package edu.andrews.cas.physics.migration.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class ResultSubscriber<T> implements Subscriber<T> {
    private static final Logger logger = LogManager.getLogger();
    private Subscription subscription;

    @Override
    public void onSubscribe(Subscription s) {
        logger.info("Received subscription: {}", s);
        this.subscription = s;
        s.request(1);
    }

    @Override
    public void onNext(T t) {
        logger.info("Successfully received result: {}", t);
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable t) {
        logger.error("A subscription error occurred", t);
        t.printStackTrace();
        System.exit(1); // TODO REMOVE IN PRODUCTION
    }

    @Override
    public void onComplete() {
        logger.error("Subscription complete: {}", this.subscription);
    }
}

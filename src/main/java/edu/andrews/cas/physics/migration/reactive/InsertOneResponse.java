package edu.andrews.cas.physics.migration.reactive;

import com.mongodb.client.result.InsertOneResult;
import org.bson.types.ObjectId;
import org.reactivestreams.Subscription;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class InsertOneResponse extends Subscriber<ObjectId, InsertOneResult> {
    public InsertOneResponse(CompletableFuture<ObjectId> future) {
        super(future);
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(1);
    }

    @Override
    public void onNext(InsertOneResult insertOneResult) {
        super.future.complete(Objects.requireNonNull(insertOneResult.getInsertedId()).asObjectId().getValue());
    }

    @Override
    public void onError(Throwable t) {
        super.future.completeExceptionally(t);
    }

    @Override
    public void onComplete() {
        super.future.complete(null);
    }
}

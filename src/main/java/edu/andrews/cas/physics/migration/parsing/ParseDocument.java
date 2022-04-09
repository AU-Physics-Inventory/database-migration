package edu.andrews.cas.physics.migration.parsing;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.concurrent.CompletableFuture;

public class ParseDocument<T> {
    private final String parseString;
    private final DocumentType type;
    private final CompletableFuture<T> future;
    private final String objectName;
    private final String identifier;

    public ParseDocument(String parseString, CompletableFuture<T> future, DocumentType type, String objectName, String identifier) {
        this.parseString = parseString;
        this.future = future;
        this.type = type;
        this.objectName = objectName;
        this.identifier = identifier;
    }

    public String getParseString() {
        return parseString;
    }

    public void parseAs(T t) {
        future.complete(t);
    }

    public DocumentType getType() {
        return type;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public enum DocumentType {
        QUANTITY, PRICE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ParseDocument<?> that = (ParseDocument<?>) o;

        return new EqualsBuilder().append(parseString, that.parseString).append(type, that.type).append(future, that.future).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(parseString).append(type).append(future).toHashCode();
    }
}

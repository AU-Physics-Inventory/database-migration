package edu.andrews.cas.physics.inventory.model.mongodb.group;

import edu.andrews.cas.physics.inventory.model.mongodb.DocumentConversion;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Group implements DocumentConversion {
    private final ObjectId _id = null;
    private final String name;
    private final List<ObjectId> assets;
    private final List<Integer> identityNos;

    public Group(String name) {
        this.name = name;
        this.assets = new ArrayList<>();
        this.identityNos = new ArrayList<>();
    }

    public Group(String name, List<ObjectId> assets, List<Integer> identityNos) {
        this.name = name;
        this.assets = assets;
        this.identityNos = identityNos;
    }

    public ObjectId get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public List<ObjectId> getAssets() {
        return assets;
    }

    public List<Integer> getIdentityNos() {
        return identityNos;
    }

    public void addAsset(ObjectId id) {
        if (!this.assets.contains(id)) this.assets.add(id);
    }

    public void addIdentityNo(int identityNo) {
        if (!this.identityNos.contains(identityNo)) this.identityNos.add(identityNo);
    }

    @Override
    public Document toDocument() {
        return new Document()
                .append("name", getName())
                .append("assets", getAssets())
                .append("identityNos", getIdentityNos());
    }
}

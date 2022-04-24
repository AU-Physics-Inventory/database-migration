package edu.andrews.cas.physics.inventory.model.mongodb.set;

import edu.andrews.cas.physics.inventory.model.mongodb.DocumentConversion;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Set implements DocumentConversion {
    private final ObjectId _id = null;
    private final String name;
    private final List<ObjectId> assets;
    private final List<Integer> identityNos;
    private final List<ObjectId> groups;

    public Set(int id, String name) {
        this.name = name;
        this.assets = new ArrayList<>();
        this.identityNos = new ArrayList<>();
        this.groups = new ArrayList<>();
    }

    public Set(String name, List<ObjectId> assets, List<Integer> identityNos, List<ObjectId> groups) {
        this.name = name;
        this.assets = assets;
        this.identityNos = identityNos;
        this.groups = groups;
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

    public List<ObjectId> getGroups() {
        return groups;
    }

    public void addAsset(ObjectId id) {
        if (!this.assets.contains(id)) this.assets.add(id);
    }

    public void addIdentityNo(int identityNo) {
        if (!this.identityNos.contains(identityNo)) this.identityNos.add(identityNo);
    }

    public void addGroup(ObjectId id) {
        if (!this.groups.contains(id)) this.groups.add(id);
    }

    @Override
    public Document toDocument() {
        return new Document()
                .append("name", getName())
                .append("assets", getAssets())
                .append("identityNos", getIdentityNos())
                .append("groups", getGroups());
    }
}
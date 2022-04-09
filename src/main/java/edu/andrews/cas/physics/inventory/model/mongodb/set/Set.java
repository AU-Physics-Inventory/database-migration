package edu.andrews.cas.physics.inventory.model.mongodb.set;

import edu.andrews.cas.physics.inventory.model.mongodb.DocumentConversion;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;

import java.util.ArrayList;
import java.util.List;

public class Set implements DocumentConversion {
    private final int _id;
    private final String name;
    private final List<Integer> assets;
    private final List<Integer> identityNos;
    private final List<Integer> groups;

    public Set(int id, String name) {
        this._id = id;
        this.name = name;
        this.assets = new ArrayList<>();
        this.identityNos = new ArrayList<>();
        this.groups = new ArrayList<>();
    }

    public Set(int id, String name, List<Integer> assets, List<Integer> identityNos, List<Integer> groups) {
        this._id = id;
        this.name = name;
        this.assets = assets;
        this.identityNos = identityNos;
        this.groups = groups;
    }

    public int get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getAssets() {
        return assets;
    }

    public List<Integer> getIdentityNos() {
        return identityNos;
    }

    public List<Integer> getGroups() {
        return groups;
    }

    public void addAsset(int id) {
        if (!this.assets.contains(id)) this.assets.add(id);
    }

    public void addIdentityNo(int identityNo) {
        if (!this.identityNos.contains(identityNo)) this.identityNos.add(identityNo);
    }

    public void addGroup(int id) {
        if (!this.groups.contains(id)) this.groups.add(id);
    }

    @Override
    public Document toDocument() {
        return new Document()
                .append("_id", get_id())
                .append("name", getName())
                .append("assets", getAssets())
                .append("identityNos", getIdentityNos())
                .append("groups", getGroups());
    }
}
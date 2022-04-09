package edu.andrews.cas.physics.inventory.model.mongodb.group;

import edu.andrews.cas.physics.inventory.model.mongodb.DocumentConversion;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;

import java.util.ArrayList;
import java.util.List;

public class Group implements DocumentConversion {
    private final int _id;
    private final String name;
    private final List<Integer> assets;
    private final List<Integer> identityNos;

    public Group(int id, String name) {
        this._id = id;
        this.name = name;
        this.assets = new ArrayList<>();
        this.identityNos = new ArrayList<>();
    }

    public Group(int id, String name, List<Integer> assets, List<Integer> identityNos) {
        this._id = id;
        this.name = name;
        this.assets = assets;
        this.identityNos = identityNos;
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

    public void addAsset(int id) {
        if (!this.assets.contains(id)) this.assets.add(id);
    }

    public void addIdentityNo(int identityNo) {
        if (!this.identityNos.contains(identityNo)) this.identityNos.add(identityNo);
    }

    @Override
    public Document toDocument() {
        return new Document()
                .append("_id", get_id())
                .append("name", getName())
                .append("assets", getAssets())
                .append("identityNos", getIdentityNos());
    }
}

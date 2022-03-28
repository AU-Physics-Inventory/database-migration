package edu.andrews.cas.physics.inventory.model.mongodb.asset;

import java.net.URL;

public record Manual(int identityNo, boolean hasHardcopy, URL softcopyURL) {};

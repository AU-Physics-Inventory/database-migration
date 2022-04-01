package edu.andrews.cas.physics.inventory.model.mongodb.asset;

public final class Manual {
    private int identityNo;
    private boolean hasHardcopy;
    private String softcopy;

    public Manual(int identityNo, boolean hasHardcopy, String softcopy) {
        this.identityNo = identityNo;
        this.hasHardcopy = hasHardcopy;
        this.softcopy = softcopy;
    }

    public int getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(int identityNo) {
        this.identityNo = identityNo;
    }

    public boolean isHasHardcopy() {
        return hasHardcopy;
    }

    public void setHasHardcopy(boolean hasHardcopy) {
        this.hasHardcopy = hasHardcopy;
    }

    public String getSoftcopy() {
        return softcopy;
    }

    public void setSoftcopy(String softcopy) {
        this.softcopy = softcopy;
    }
}

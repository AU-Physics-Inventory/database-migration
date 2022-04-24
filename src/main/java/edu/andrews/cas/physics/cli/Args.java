package edu.andrews.cas.physics.cli;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = "-receipts", description = "path to serialized receipts map")
    private String receiptsPath;

    @Parameter(names = "-images", description = "path to serialized images map")
    private String imagesPath;

    @Parameter(names = "-assets", description = "path to serialized assets map")
    private String assetsPath;

    @Parameter(names = "-sets", description = "path to serialized sets map")
    private String setsPath;

    @Parameter(names = "-groups", description = "path to serialized groups map")
    private String groupsPath;

    public String getReceiptsPath() {
        return receiptsPath;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public String getAssetsPath() {
        return assetsPath;
    }

    public String getSetsPath() {
        return setsPath;
    }

    public String getGroupsPath() {
        return groupsPath;
    }
}
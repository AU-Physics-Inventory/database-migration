package edu.andrews.cas.physics.cli;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = "-receipts", description = "path to serialized receipts map")
    private String receiptsPath;

    @Parameter(names = "-images", description = "path to serialized images map")
    private String imagesPath;

    public String getReceiptsPath() {
        return receiptsPath;
    }

    public String getImagesPath() {
        return imagesPath;
    }
}
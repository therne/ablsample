package org.airbloc.airblocsample2.utils;

import org.airbloc.airblocsample2.Constants;

/**
 * Builds image URL.
 */
public class ImageUrl {

    public String imageId;
    public int width = -1, height = -1;

    public ImageUrl(String imageId) {
        this.imageId = imageId;
    }

    public ImageUrl(String imageId, int width) {
        this.imageId = imageId;
        this.width = width;
    }

    public ImageUrl(String imageId, int width, int height) {
        this.imageId = imageId;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return String.format("%s/image/%s%s", Constants.ENDPOINT, imageId,
                width == -1 ? "" : (height == -1 ? "/" + width : "/" + width + "/" + height));
    }
}

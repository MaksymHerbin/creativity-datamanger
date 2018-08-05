package com.creativity.datamanager.attributes;

import com.creativity.datamanager.domain.Photo;

public interface AttributesEnricher {

    void enrich(Photo photo);

    boolean checkEnriched(Photo photo);

    default void enrichIfMissing(Photo photo) {
        if (!checkEnriched(photo)) {
            enrich(photo);
        }
    }
}

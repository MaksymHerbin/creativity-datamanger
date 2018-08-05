package com.creativity.datamanager.attributes;

import com.creativity.datamanager.domain.Photo;

import java.util.List;

public class ChainOfAttributeEnrichers implements AttributesEnricher {

    private final List<AttributesEnricher> attributesEnrichers;

    public ChainOfAttributeEnrichers(List<AttributesEnricher> attributesEnrichers) {
        this.attributesEnrichers = attributesEnrichers;
    }

    @Override
    public void enrich(Photo photo) {
        attributesEnrichers.forEach(enricher -> enricher.enrich(photo));
    }

    @Override
    public boolean checkEnriched(Photo photo) {
        return attributesEnrichers.stream().allMatch(enricher -> enricher.checkEnriched(photo));
    }

    @Override
    public void enrichIfMissing(Photo photo) {
        attributesEnrichers.forEach(enricher -> enricher.enrichIfMissing(photo));
    }

}

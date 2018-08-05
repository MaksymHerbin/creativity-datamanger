package com.creativity.datamanager.attributes;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.creativity.datamanager.attributes.labels.LabelsEnricher;
import com.creativity.datamanager.compression.ImageCompressor;
import com.creativity.datamanager.file.storage.PhotosStorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AttributeEnrichersConfiguration {

    @Bean
    public AttributesEnricher attributeEnricher(AmazonRekognition amazonRekognition,
                                                PhotosStorageService photosStorageService,
                                                ImageCompressor imageCompressor) {
        List<AttributesEnricher> enrichers = new ArrayList<>();
        enrichers.add(new LabelsEnricher(photosStorageService, imageCompressor, amazonRekognition));
        return new ChainOfAttributeEnrichers(enrichers);
    }

}

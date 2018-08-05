package com.creativity.datamanager.attributes.labels;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.creativity.datamanager.attributes.AttributesEnricher;
import com.creativity.datamanager.compression.ImageCompressor;
import com.creativity.datamanager.domain.Photo;
import com.creativity.datamanager.file.storage.PhotosStorageService;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class LabelsEnricher implements AttributesEnricher {

    private static final float MIN_CONFIDENCE = 75F;
    private static final int MAX_LABELS = 100;

    private final PhotosStorageService photosStorageService;
    private final ImageCompressor imageCompressor;
    private final AmazonRekognition recognitionClient;

    public LabelsEnricher(PhotosStorageService photosStorageService, ImageCompressor imageCompressor, AmazonRekognition recognitionClient) {
        this.photosStorageService = photosStorageService;
        this.imageCompressor = imageCompressor;
        this.recognitionClient = recognitionClient;
    }

    @Override
    public void enrich(Photo photo) {
        File photoFile = photosStorageService.getPhoto(photo.getId());
        byte[] compressedPhoto = imageCompressor.compress(photoFile);
        Set<String> labels = getLabels(compressedPhoto).stream().map(Label::getName).collect(toSet());
        photo.setLabels(labels);
    }

    @Override
    public boolean checkEnriched(Photo photo) {
        return photo.getLabels() != null;
    }

    private List<Label> getLabels(byte[] compressedImage) {
        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image().withBytes(ByteBuffer.wrap(compressedImage)))
                .withMaxLabels(MAX_LABELS)
                .withMinConfidence(MIN_CONFIDENCE);

        DetectLabelsResult result = recognitionClient.detectLabels(request);
        return result.getLabels();
    }

}

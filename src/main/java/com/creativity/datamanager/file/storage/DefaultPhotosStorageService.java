package com.creativity.datamanager.file.storage;

import com.creativity.datamanager.compression.ImageCompressor;
import com.creativity.datamanager.domain.Photo;
import com.google.common.io.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class DefaultPhotosStorageService implements PhotosStorageService {

    @Autowired
    private ImageCompressor imageCompressor;

    @Override
    public Set<String> copyFile(String albumName, File targetFolder, File sourceFolder) throws IOException {
        Set<String> fileIds = new HashSet<>();

        if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
            throw new IllegalArgumentException(sourceFolder + " must be a folder");
        }
        for (File photo : sourceFolder.listFiles(file -> !file.getName().equals(".DS_Store"))) {
            if (photo.isFile()) {
                String newId = albumName + "-" + UUID.randomUUID().toString() + ".png";
                File targetFile = new File(targetFolder.getAbsolutePath() + "/" + newId);
                Files.write(imageCompressor.compress(photo), targetFile);
                fileIds.add(newId);
            }
        }
        return fileIds;
    }

    @Override
    public boolean photoFileExists(Photo photo, File targetFolder) {
        return new File(targetFolder.getAbsolutePath() + "/" + photo.getId()).exists();
    }
}

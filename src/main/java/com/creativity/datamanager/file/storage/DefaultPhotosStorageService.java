package com.creativity.datamanager.file.storage;

import com.creativity.datamanager.domain.Photo;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class DefaultPhotosStorageService implements PhotosStorageService {

    private final File targetPhotosFolder;

    public DefaultPhotosStorageService() {
        String targetPhotosFolder = System.getenv("default.target.folder");
        Preconditions.checkNotNull(targetPhotosFolder, "default.target.folder environment variable was not specified, and is mandatory");
        this.targetPhotosFolder = new File(targetPhotosFolder);
    }

    @Override
    public Set<String> copyFile(String albumName, File sourceFolder) throws IOException {
        Set<String> fileIds = new HashSet<>();

        if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
            throw new IllegalArgumentException(sourceFolder + " must be a folder");
        }
        for (File photo : sourceFolder.listFiles(file -> !file.getName().equals(".DS_Store"))) {
            if (photo.isFile()) {
                String newId = albumName + "-" + UUID.randomUUID().toString() + ".png";
                File targetFile = new File(targetPhotosFolder.getAbsolutePath() + "/" + newId);
                Files.copy(photo, targetFile);
                fileIds.add(newId);
            }
        }
        return fileIds;
    }

    @Override
    public File getTargetFolder() {
        return targetPhotosFolder;
    }

    @Override
    public boolean photoFileExists(Photo photo) {
        return new File(targetPhotosFolder.getAbsolutePath() + "/" + photo.getId()).exists();
    }

    @Override
    public File getPhoto(String id) {
        return new File(targetPhotosFolder.getAbsolutePath() + "/" + id);
    }
}

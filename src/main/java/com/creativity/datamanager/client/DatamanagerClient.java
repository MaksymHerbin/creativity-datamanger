package com.creativity.datamanager.client;

import com.creativity.datamanager.domain.Photo;
import com.creativity.datamanager.file.storage.PhotosStorageService;
import com.creativity.datamanager.repository.ExampleRepository;
import com.creativity.datamanager.repository.PhotoRepository;
import com.drew.imaging.ImageProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.springframework.shell.Availability.available;
import static org.springframework.shell.Availability.unavailable;

@ShellComponent
public class DatamanagerClient {

    private File targetPhotosFolder;

    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private ExampleRepository exampleRepository;
    @Autowired
    private PhotosStorageService photosStorageService;

    public DatamanagerClient() {
        String defaultTargetPhotosFolder = System.getenv("default.target.folder");
        if (defaultTargetPhotosFolder != null) {
            targetPhotosFolder = new File(defaultTargetPhotosFolder);
        }
    }

    @ShellMethod("List all existing photos in database")
    @ShellMethodAvailability("noExampleInCreation")
    public List<Photo> list_all() {
        return photoRepository.findAll();
    }

    @ShellMethod("Register target folder for photos location")
    @ShellMethodAvailability("specifyPhotosFolder")
    public String photos_folder(String folderPath) {
        File candidate = new File(folderPath);
        if (!candidate.exists()) {
            return "Path specified " + folderPath + " does not exist";
        } else if (!candidate.isDirectory()) {
            return "Path specified " + folderPath + " is not a directory";
        } else {
            this.targetPhotosFolder = candidate;
            return "Successfully registered " + targetPhotosFolder.getAbsolutePath() + " as photos storage";
        }
    }

    @ShellMethod("Load photos from specified folder")
    @ShellMethodAvailability("operationWithPhotos")
    public String load_photos(String albumName, String downloadFrom) throws IOException, ImageProcessingException {
        Set<String> filesCreated = photosStorageService.copyFile(albumName, targetPhotosFolder, new File(downloadFrom));
        for (String photoId : filesCreated) {
            photoRepository.save(new Photo(photoId));
        }
        return "Successfully copied all files from folder " + downloadFrom + " to " + targetPhotosFolder.getAbsolutePath() + "\n Ids:\n" + filesCreated;
    }

    @ShellMethod("Validate all photos in database have matching file in target folder")
    @ShellMethodAvailability("operationWithPhotos")
    public String validate_photo_files_vs_db() {
        Set<String> photosIdThatWereNotFound = photoRepository.findAll().stream()
                .filter(photo -> !photosStorageService.photoFileExists(photo, targetPhotosFolder))
                .map(Photo::getId)
                .collect(toSet());
        return "Following photos existing in database do not exist in " + targetPhotosFolder.getAbsolutePath() + ":\n" + photosIdThatWereNotFound;
    }

    @ShellMethod("Removes all photos record from database for with file does not exist in target folder")
    @ShellMethodAvailability("operationWithPhotos")
    public String clear_obsolete_files_from_db() {
        photoRepository.findAll().stream()
                .filter(photo -> !photosStorageService.photoFileExists(photo, targetPhotosFolder))
                .forEach(photoRepository::delete);
        return "Cleared database from obsolete photos";
    }

    private Availability operationWithPhotos() {
        return targetPhotosFolder == null ? unavailable("Target folder for loading photos not yet specified") : available();
    }

    private Availability specifyPhotosFolder() {
        return targetPhotosFolder == null ? available() : unavailable("Target folder for loading photos already specified " + targetPhotosFolder);
    }

}

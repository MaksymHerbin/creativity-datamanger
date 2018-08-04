package com.creativity.datamanager.compression;

import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@Service
public class JavaApiImageCompressor implements ImageCompressor {

    private static final float COMPRESSION_QUALITY = 0.2f;

    @Override
    public byte[] compress(File original) {
        try {
            BufferedImage image = ImageIO.read(original);

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = writers.next();

            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(COMPRESSION_QUALITY);
            writer.write(null, new IIOImage(image, null, null), param);

            os.close();
            ios.close();
            writer.dispose();

            return os.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error compressing image " + original.getAbsolutePath(), e);
        }

    }
}

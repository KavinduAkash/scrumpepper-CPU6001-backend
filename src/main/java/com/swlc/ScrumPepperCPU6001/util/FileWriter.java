package com.swlc.ScrumPepperCPU6001.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.exception.FileUploadException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author hp
 */
@Component
@Log4j2
public class FileWriter {

    @Autowired
    private static HttpServletRequest request;


//    public String saveMultipartFile(MultipartFile file, String type) {
//
//        log.info("Start Function saveMultipartFile " + type);
//        File serverFile = null;
//        try {
//            String fileUrl = null;
//            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
//            String fileUploadDir = null;
//            String fileDownloadDir = null;
//            String name = UUID.randomUUID().toString();
//
//            switch (type) {
//                case "corporate_logo":
//                    fileDownloadDir = outgoingURL + FOLDER_PATH_E_TENDER_SELLER_BILLING_PROOF;
//                    fileUploadDir = FOLDER_PATH_E_TENDER_SELLER_BILLING_PROOF.substring(1);
//                    break;
//            }
//
//            String originalFilename = file.getOriginalFilename();
//            if (originalFilename != null) {
//                String[] split = originalFilename.split("\\.");
//                extension = split[(split.length - 1)];
//            }
//
//
//            String fileName = name + FilenameUtils.EXTENSION_SEPARATOR_STR + extension;
//
//            serverFile = convertMultiPartToFile(file, fileName);
//
//            uploadFileTos3bucket(fileUploadDir + fileName, serverFile);
//
//            fileUrl = fileDownloadDir + fileName;
//
//
//            log.info("Function saveMultipartFile ATTACHMENT PATH : " + fileUrl);
//
//            return fileUrl;
//
//        } catch (Exception e) {
//            log.error("Function saveMultipartFile : " + e.getMessage(), e);
//            e.printStackTrace();
//            throw new ETendersServiceException(MULTIPART_FILE_SAVE_ERROR, "Error occurred while saving attachment");
//        } finally {
//            if (bufferedOutputStream != null) {
//                try {
//                    bufferedOutputStream.close();
//                    if (serverFile != null) serverFile.delete();
//                } catch (IOException e) {
//                    log.error("Function saveMultipartFile : " + e.getMessage(), e);
//                    throw new ETendersServiceException(MULTIPART_FILE_SAVE_ERROR, "Error occurred while saving attachment");
//                }
//            }
//        }
//
//    }
//
//
//    private String getExtension(String base64ImageString) {
//        try {
//            String delims = "[,]";
//            String[] parts = base64ImageString.split(delims);
//            String imageString = parts[1];
//            byte[] imageByteArray = Base64.decode(imageString);
//
//            InputStream is = new ByteArrayInputStream(imageByteArray);
//
//            //Find out image type
//            String mimeType = null;
//            String fileExtension = null;
//
//            mimeType = URLConnection.guessContentTypeFromStream(is); //mimeType is something like "image/jpeg"
//            String delimiter = "[/]";
//            String[] tokens = mimeType.split(delimiter);
//            fileExtension = tokens[1];
//            return String.format(EXTENSION_FORMAT, fileExtension);
//        } catch (Exception e) {
//            log.error("Function getExtension : " + e.getMessage());
//            return DEFAULT_EXTENSION;
//        }
//    }
//
//    private boolean isMatch(byte[] pattern, byte[] data) {
//        if (pattern.length <= data.length) {
//            for (int idx = 0; idx < pattern.length; ++idx) {
//                if (pattern[idx] != data[idx])
//                    return false;
//            }
//            return true;
//        }
//        return false;
//    }
//
//    private String getImageType(byte[] data) {
//
////        filetype    magic number(hex)
////        jpg         FF D8 FF
////        gif         47 49 46 38
////        png         89 50 4E 47 0D 0A 1A 0A
////        bmp         42 4D
////        tiff(LE)    49 49 2A 00
////        tiff(BE)    4D 4D 00 2A
//
//        final byte[] pngPattern = new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
//        final byte[] jpgPattern = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
//        final byte[] gifPattern = new byte[]{0x47, 0x49, 0x46, 0x38};
//        final byte[] bmpPattern = new byte[]{0x42, 0x4D};
//        final byte[] tiffLEPattern = new byte[]{0x49, 0x49, 0x2A, 0x00};
//        final byte[] tiffBEPattern = new byte[]{0x4D, 0x4D, 0x00, 0x2A};
//
//        if (isMatch(pngPattern, data))
//            return "png";
//
//        if (isMatch(jpgPattern, data))
//            return "jpg";
//
//        if (isMatch(gifPattern, data))
//            return "gif";
//
//        if (isMatch(bmpPattern, data))
//            return "bmp";
//
//        if (isMatch(tiffLEPattern, data))
//            return "tif";
//
//        if (isMatch(tiffBEPattern, data))
//            return "tif";
//
//        return "png";
//    }
//
//    // Convert any multipart file to file
//    private File convertMultiPartToFile(MultipartFile file, String fileName) throws IOException {
//        log.info("Start function convertMultiPartToFile @ param fileName : " + fileName);
//        FileOutputStream fos = null;
//        try {
//            Path path = Paths.get(TEMP_FILE_PATH, fileName);
//            File convFile = new File(path.toString());
//            fos = new FileOutputStream(convFile);
//            fos.write(file.getBytes());
//            return convFile;
//        } catch (Exception e) {
//            log.error("convertMultiPartToFile : {}", e.getMessage());
////            throw new B2BServiceException(MULTIPART_FILE_SAVE_ERROR, "Error occurred while converting multipart file to file");
//            throw e;
//        } finally {
//            if (fos != null) fos.close();
//        }
//
//    }


    public String saveMultipartImage(MultipartFile file) {
        try {
            String uploadsDir = "/uploads/";

            log.info("realPathtoUploads 1");

//            String realPathtoUploads =  request.getServletContext().getRealPath(uploadsDir);
//
//            log.info("realPathtoUploads 2");
//
//            if(! new File(realPathtoUploads).exists())
//            {
//                log.info("realPathtoUploads 3");
//                new File(realPathtoUploads).mkdir();
//            }


            log.info("realPathtoUploads 4");

            log.info("realPathtoUploads 5");
            String orgName = file.getOriginalFilename();
            String filePath = "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.5\\webapps\\uploads\\" + orgName;
            String filePath2 = "http://localhost:8080/uploads/" + orgName;
            File dest = new File(filePath);
            file.transferTo(dest);

            return filePath2;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new FileUploadException(ApplicationConstant.UNABLE_UPLOAD_FILE, "Unable to upload image");
        } catch (Exception e) {
            throw e;
        }
    }

}

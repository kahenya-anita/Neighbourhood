package com.neighbourhood.online.neighbourhood.services;

import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;


@AllArgsConstructor
public class BASE64DecodedMultipartFile implements MultipartFile {

    private final byte[] multipartContent;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getOriginalFilename() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return multipartContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return multipartContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(multipartContent);
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
        new FileOutputStream(file).write(multipartContent);
    }
}
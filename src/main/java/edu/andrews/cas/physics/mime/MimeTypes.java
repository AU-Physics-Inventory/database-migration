package edu.andrews.cas.physics.mime;

import java.util.ArrayList;

public class MimeTypes {
    private static final ArrayList<MimeType> mimeTypes = new ArrayList<>();
    
    static {
        mimeTypes.add(new MimeType("image/png", new byte[]{(byte) 137, 80, 78, 71, 13, 10, 26, 10}));
        mimeTypes.add(new MimeType("application/pdf", new byte[]{'%', 'P', 'D', 'F'}));
        mimeTypes.add(new MimeType("image/jpeg", new byte[]{(byte) 0xFF, (byte) 0xD8}));
        mimeTypes.add(new MimeType("image/tiff", new byte[]{0x49, 0x49, 0x2A, 0x00}));
        mimeTypes.add(new MimeType("image/tiff", new byte[]{0x4D, 0x4D, 0x00, 0x2A}));
        mimeTypes.add(new MimeType("image/bmp", new byte[]{'B', 'M'}));
        mimeTypes.add(new MimeType("image/bmp", new byte[]{'B', 'A'}));
        mimeTypes.add(new MimeType("image/bmp", new byte[]{'C', 'I'}));
        mimeTypes.add(new MimeType("image/bmp", new byte[]{'C', 'P'}));
        mimeTypes.add(new MimeType("image/bmp", new byte[]{'I', 'C'}));
        mimeTypes.add(new MimeType("image/bmp", new byte[]{'P', 'T'}));
    }

    record MimeType(String mediaType, byte[] bytes) {
        public boolean compare(byte[] arr) {
            for (int i = 0; i < bytes.length; i++) {
                if (bytes[i] != arr[i]) return false;
            }

            return true;
        }
    }

    public static String detect(byte[] arr) {
        for (MimeType type : mimeTypes) {
            if (type.compare(arr)) return type.mediaType();
        }

        return "application/octect-stream";
    }
}

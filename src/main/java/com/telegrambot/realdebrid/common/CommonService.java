package com.telegrambot.realdebrid.common;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class CommonService {

    public String readJSONFromInputStream(InputStream inputStream) throws IOException {
        return  IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

    public String getHashFromMagnet(String magnet) {
        return magnet.substring(20, 60);
    }
}

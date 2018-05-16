package com.lsl.d;

import java.io.IOException;
import java.io.InputStream;

public interface IDowload {
    InputStream getStream(String uri) throws IOException;
}

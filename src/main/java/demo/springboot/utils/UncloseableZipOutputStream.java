package demo.springboot.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

public class UncloseableZipOutputStream extends ZipOutputStream {
    OutputStream os;

    public UncloseableZipOutputStream( OutputStream os )
    {
        super(os);
    }

    @Override
    /** just flush but do not close */
    public void close() throws IOException
    {
        flush();
    }

    public void reallyClose() throws IOException
    {
        super.close();
    }
}

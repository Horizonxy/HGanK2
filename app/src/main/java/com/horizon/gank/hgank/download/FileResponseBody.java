package com.horizon.gank.hgank.download;

import com.horizon.gank.hgank.util.BusEvent;
import com.mcxiaoke.bus.Bus;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

/**
 * Created by Administrator on 2016/8/18.
 */
public class FileResponseBody extends ResponseBody {

    private Response response;
    private BufferedSource bufferedSource;

    public FileResponseBody(Response response){
        this.response = response;
    }

    @Override
    public MediaType contentType() {
        return response.body().contentType();
    }

    @Override
    public long contentLength() {
        return response.body().contentLength();
    }

    @Override
    public BufferedSource source() {
        if(bufferedSource == null) {
            bufferedSource = Okio.buffer(new ForwardingSource(response.body().source()) {
                long readed;
                long lastReaded;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long read = super.read(sink, byteCount);
                    readed += read == -1 ? 0 : read;

                    if ((readed - lastReaded) > contentLength() * 3 / 100 || readed >= contentLength()) {
                        Bus.getDefault().postSticky(new BusEvent.FileDownLoadEvent(readed, contentLength()));
                        lastReaded = readed;
                    }

                    return read;
                }
            });
        }
        return bufferedSource;
    }

}

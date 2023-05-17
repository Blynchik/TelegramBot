package ru.relex.utils;

import org.hashids.Hashids;

public class CryptoTool {
    private final Hashids hashids;

    public CryptoTool(String salt) {
        //минимальная длина получаемой записи
        var minHashLength = 10;
        //хэшируемый объект шифруется с помощью соли
        this.hashids = new Hashids(salt, minHashLength);
    }

    //шифрует
    public String hashOf(Long value) {
        return hashids.encode(value);
    }

    //дешифрует
    public Long idOf(String value) {
        long[] res = hashids.decode(value);
        if (res != null && res.length > 0) {
            return res[0];
        }
        return null;
    }
}

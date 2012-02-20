package org.bouncycastle.util;

import java.util.Collection;

@SuppressWarnings({ "rawtypes" })
public interface Store {
    Collection getMatches(Selector selector) throws StoreException;
}

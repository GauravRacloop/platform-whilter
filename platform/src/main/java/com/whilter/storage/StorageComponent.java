package com.whilter.storage;

import com.whilter.core.Component;

/**
 * Created by deepakchauhan on 10/09/17.
 */
public interface StorageComponent<E extends StorageEndpoint, S extends Storage> extends Component<E, S> {

}

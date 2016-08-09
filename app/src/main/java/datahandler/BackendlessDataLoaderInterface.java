package datahandler;

import java.util.ArrayList;

/**
 * Created by Mikko on 29.7.2016.
 */
public interface BackendlessDataLoaderInterface<T> {

    void loadSuccessful(ArrayList<T> objectList);
    void loadFailed();
}

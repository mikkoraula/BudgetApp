package datahandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mikko on 29.7.2016.
 */
public interface BackendlessDataLoaderInterface<T> {

    void loadSuccessful(ArrayList<T> objectList);
    void loadFailed();
}

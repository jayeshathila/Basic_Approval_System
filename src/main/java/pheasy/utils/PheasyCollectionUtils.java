package pheasy.utils;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Map;

/**
 * Created by jayeshathila
 * on 13/06/17.
 */
public class PheasyCollectionUtils {

    public static <K, V> Map<K, V> transformMap(Iterable<? extends V> values, Function<? super V, K> keyFunction) {
        if (values == null) {
            return Collections.emptyMap();
        }
        return Maps.<K, V>newHashMap(Maps.uniqueIndex(values.iterator(), keyFunction));
    }

}

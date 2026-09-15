package com.kailin.util;

import com.kailin.common.ErrorKRMessage;
import com.kailinjt.middleware.kp.common.api.exception.KBException;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zh_o
 */
public class OptionalUtils {

    public static <T> T getOrElseThrow(T obj, ErrorKRMessage emsg) {
        return Optional.ofNullable(obj).orElseThrow(() -> new KBException(emsg));
    }

    public static <T, R> List<R> map(Collection<T> collection, Function<T, R> mapper) {
        return collection.stream().filter(Objects::nonNull).map(mapper).collect(Collectors.toList());
    }

    public static <T, R> List<R> mapAndDistinct(Collection<T> collection, Function<T, R> mapper) {
        return collection.stream().filter(Objects::nonNull).map(mapper).distinct().collect(Collectors.toList());
    }

}

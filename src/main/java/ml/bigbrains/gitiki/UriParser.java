package ml.bigbrains.gitiki;

import ml.bigbrains.gitiki.model.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class UriParser {
    private static final Logger log = LoggerFactory.getLogger(UriParser.class);

    public static Request inputToRequest(String input)
    {
        if(input==null)
            return new Request(new ArrayList<>(), "");
        input = input.trim();
        String [] params = input.split("/");
        if(params.length==1)
            return new Request(new ArrayList<>(), params[0]);
        if(params.length>1) {
            List<String> paths = Stream.of(params).limit(params.length-1).filter(Predicate.not(String::isEmpty)).toList();
            return new Request(paths, params[params.length-1]);
        }
        return new Request(new ArrayList<>(), "");
    }
}

package ml.bigbrains.gitiki;

import ml.bigbrains.gitiki.model.Request;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class UriParserTest {
    @Test
    public void testNullUri()
    {
        Request request = UriParser.inputToRequest(null);
        Assert.notNull(request, "Error if input URI is null");
    }
    @Test
    public void testInputUri()
    {
        Assert.isTrue(UriParser.inputToRequest("").path().size()==0, "Wrong number of subdirectory of root");
        Assert.isTrue(UriParser.inputToRequest("                    ").path().size()==0, "Wrong number of subdirectory of root");
        Assert.isTrue(UriParser.inputToRequest("/index").path().size()==0, "Wrong number of subdirectory with index page");
        Assert.isTrue(UriParser.inputToRequest("/").path().size()==0, "Wrong number of subdirectory of root");
        Assert.isTrue(UriParser.inputToRequest("/index/sub/directory/many/one").path().size()==4, "Wrong number of subdirectory");
        Assert.isTrue(UriParser.inputToRequest("/index/sub/directory/many/one").name().equals("one"), "Wrong name");
        Assert.isTrue(UriParser.inputToRequest("/index").name().equals("index"), "Wrong name");
        Assert.isTrue(UriParser.inputToRequest("/").name().equals(""), "Wrong name");
        Assert.isTrue(UriParser.inputToRequest("").name().equals(""), "Wrong name");
        Assert.isTrue(UriParser.inputToRequest("                ").name().equals(""), "Wrong name: "+UriParser.inputToRequest("                ").name());
    }
}

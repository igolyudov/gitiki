package ml.bigbrains.gitiki;

import jakarta.servlet.http.HttpServletRequest;
import ml.bigbrains.gitiki.model.Request;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    private TemplateUtils templateUtils;
    private ContentReader contentReader;

    public MainController(TemplateUtils templateUtils, ContentReader contentReader) {
        this.templateUtils = templateUtils;
        this.contentReader = contentReader;
    }

    @RequestMapping("/**")
    public String getVersion(HttpServletRequest req, @RequestParam("version") Optional<String> version)
    {
        log.info("Request path: {}, version: {}",req.getRequestURI(), version);
        return getContent(UriParser.inputToRequest(req.getRequestURI()), version.orElse(null));
    }

    private String getContent(Request request, String version)
    {
        return getContent(request.path(),request.name(), version);
    }

    private String getContent(List<String> dir, String pageName, String version)
    {
        if(dir==null || dir.isEmpty())
            dir=new ArrayList<>();
        if(!dir.isEmpty() && dir.get(0).equals("static")) {
            return contentReader.getStatic(dir,pageName);
        }
        if(pageName==null || pageName.isEmpty() || pageName.equals(""))
            pageName = "index";
        String content = contentReader.getPageContent(dir, pageName, version);
        String template = templateUtils.getTemplate();
        Parser parser = Parser.builder().build();
        Node document = parser.parse(content);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return template.replace("<gitiki_content/>",renderer.render(document));
    }
}

package ml.bigbrains.gitiki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class TemplateUtils {

    private static final Logger log = LoggerFactory.getLogger(TemplateUtils.class);

    @Value("${gitiki.template}")
    private String templateName;

    public String getTemplate()
    {
        String templateContent="";
        try {
            File templateFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "templates" + File.separator + templateName + ".html");
            templateContent = new String(Files.readAllBytes(templateFile.toPath()));
        }
        catch (FileNotFoundException e)
        {
            log.error("Error in read template from file: {}.html",templateName,e);
        }
        catch (IOException ex)
        {
            log.error("Error in read content from file: {}.html",templateName,ex);
        }
        return templateContent;
    }
}

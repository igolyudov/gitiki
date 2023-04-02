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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ContentReader {
    private static final Logger log = LoggerFactory.getLogger(ContentReader.class);

    @Value("${gitiki.root}")
    private String contentRoot;
    @Value("${gitiki.static:}")
    private String staticRoot;

    private GitClient gitClient;

    public ContentReader(GitClient gitClient) {
        this.gitClient = gitClient;
    }

    public String getPageContent(List<String> dir, String pageName, String version)
    {
        String content="";
        try {
            String dirPath = !dir.isEmpty()?String.join("/",dir)+File.separator:"";
            content = new String(gitClient.getContentFromCommit(dirPath+pageName+".md", version));
        }
        catch (FileNotFoundException e)
        {
            log.error("Error in get content from file: {}/{}.md", dir ,pageName,e);
        }
        catch (IOException ex)
        {
            log.error("Error in read content from file: {}/{}.md",dir,pageName,ex);
        }
        return content;
    }

    public String getStatic(List<String> dir, String pageName)
    {
        String content="";
        try {
            Path path;
            if(staticRoot==null || staticRoot.isEmpty() || staticRoot.startsWith("classpath:"))
            {
                path = Paths.get( ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+String.join("/", dir) +File.separator+ pageName).getPath());
            }
            else {
                path = Paths.get(staticRoot, String.join("/", dir), pageName);
            }
            content = new String(Files.readAllBytes(path));
        }
        catch (FileNotFoundException e)
        {
            log.error("Error in get static content from file: {}/{}", dir ,pageName,e);
        }
        catch (IOException ex)
        {
            log.error("Error in read static content from file: {}/{}",dir,pageName,ex);
        }
        return content;
    }
}

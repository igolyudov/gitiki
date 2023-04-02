package ml.bigbrains.gitiki;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.notes.Note;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class GitClient {

    private static final Logger log = LoggerFactory.getLogger(GitClient.class);

    @Value("${gitiki.root}")
    private String gitRoot;

    Repository repository;

    public String getContentFromCommit(String path, String commit) throws FileNotFoundException, IOException
    {
        if(StringUtils.isEmptyOrNull(path)) {
            log.warn("Cannot get commit {} for path {}");
            return null;
        }
        if(commit==null)
            commit = Constants.HEAD;
        else if( commit.length()>40)
        {
            log.warn("Wrong commit id {}. Size: {} greate when 40 for default commit hash size",commit.length());
            return null;
        }
        String content = null;
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            repository = builder.findGitDir(new File(gitRoot))
//                    .readEnvironment() // scan environment GIT_* variables
//                    .findGitDir() // scan up the file system tree
                    .build();
            // find the HEAD
            ObjectId lastCommitId = repository.resolve(commit);

            // a RevWalk allows to walk over commits based on some filtering that is defined
            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit revCommit = revWalk.parseCommit(lastCommitId);
                // and using commit's tree find the path
                RevTree tree = revCommit.getTree();
                log.debug("Having tree: " + tree);

                // now try to find a specific file
                try (TreeWalk treeWalk = new TreeWalk(repository)) {
                    treeWalk.addTree(tree);
                    treeWalk.setRecursive(true);
                    treeWalk.setFilter(PathFilter.create(path));
                    if (!treeWalk.next()) {
                        log.error("Did not find expected file '{}'", path);
                        throw new FileNotFoundException("Did not find expected file '"+path+"'");
                    }

                    ObjectId objectId = treeWalk.getObjectId(0);
                    ObjectLoader loader = repository.open(objectId);

                    // and then one can the loader to read the file
//                    loader.copyTo(System.out);
                    content = new String(loader.getBytes());
                }

                revWalk.dispose();
            }
        }
        catch (Exception e)
        {
            log.error("Error in get git-log",e);
            throw new IOException("Error in get git-log^"+e.getMessage());
        }
        return content;
    }

}

package ml.bigbrains.gitiki;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@SpringBootTest
class GitikiApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(GitikiApplicationTests.class);
	@Autowired
	private GitClient gitClient;

	@Test
	void contextLoads() {
	}


	@Test
	public void testGitGetContentFromCommit() throws FileNotFoundException, IOException
	{
		String content = gitClient.getContentFromCommit("markdown/test/test.md", "d0a040b8ce8174412aebf2d66f0ab826cbcb170");
		log.info("CONTENT: {}",content);
//		List<Date> dates = gitClient.getFileVersionDateList("index.md");
//		log.info("{}",dates);
	}

}

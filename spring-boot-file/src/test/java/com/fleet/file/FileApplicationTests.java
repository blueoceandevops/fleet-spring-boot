package com.fleet.file;

import com.fleet.file.config.FileConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileApplicationTests {

    @Autowired
    FileConfig fileConfig;

    @Test
    public void pushAndroid() {
        System.out.println(fileConfig.getFilePath());
    }

}

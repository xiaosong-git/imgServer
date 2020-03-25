package com.xdream.goldccm.third;

import com.xdream.goldccm.service.file.IFileService;
import com.xdream.uaas.model.compose.TableList;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext.xml"})
public class ThreadConfigTest {

    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private IFileService fileService;
    @Test
    public void taskExecutor() {

        for (int i=1;i<100;i++){

            final String finalPath="D:/测试/test"+i+".txt";

            final int finalI = i;
            taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("这是线程"+ finalI);
                try {
                    int count = fileService.batchUpdate(finalPath, TableList.IN_OUT, "null,null,null");

                    System.out.println("插入数据量："+count);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test() {
        String path="D:/测试/test.txt";
        String path1="D:/测试/test";
        File f =new File(path);
        for (int i=1;i<100;i++) {
            File file =new File(path1+i+".txt");
            try {
                FileUtils.copyFile(f, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void test1() {
        String path="D:/测试/test.txt";
        String path1="D:/测试/test";
        File f =new File(path);
        for (int i=1;i<100;i++) {
            File file =new File(path1+i+".txt");
            try {
                FileUtils.copyFile(f, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

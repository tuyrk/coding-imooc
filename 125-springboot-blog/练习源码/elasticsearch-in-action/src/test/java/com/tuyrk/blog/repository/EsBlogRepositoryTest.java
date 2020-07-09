package com.tuyrk.blog.repository;

import com.tuyrk.blog.ApplicationTests;
import com.tuyrk.blog.domain.es.EsBlog;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.Assert.*;

public class EsBlogRepositoryTest extends ApplicationTests {
    @Autowired
    private EsBlogRepository esBlogRepository;

    @Before
    public void initRepositoryData() {
        // 清除所有数据
        esBlogRepository.deleteAll();
        esBlogRepository.save(new EsBlog("登鹤雀楼", "王之涣的登鹤雀楼",
                "白日依山尽，黄河入海流。欲穷千里目，更上一层楼。"));
        esBlogRepository.save(new EsBlog("相思", "王维的相思",
                "红豆生南国，春来发几枝。愿君多采撷，此物最相思。"));
        esBlogRepository.save(new EsBlog("静夜思", "李白的静夜思",
                "床前明月光，疑是地上霜。举头望明月，低头思故乡。"));
    }

    @Test
    public void findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining() {
        Pageable pageable = PageRequest.of(0, 20);
        String title = "思";
        String summary = "相思";
        String content = "相思";
        Page<EsBlog> page = esBlogRepository.
                findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining(title, summary, content, pageable);
        assertEquals(page.getTotalElements(), 2);
        List<EsBlog> esBlogList = page.getContent();
        System.out.println("======开始 1======");
        for (EsBlog esBlog : esBlogList) {
            System.out.println(esBlog);
        }
        System.out.println("======结束 1======");

        title = "相思";
        page = esBlogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining(title, summary, content, pageable);
        assertEquals(page.getTotalElements(), 1);
        esBlogList = page.getContent();
        System.out.println("======开始 2======");
        for (EsBlog esBlog : esBlogList) {
            System.out.println(esBlog);
        }
        System.out.println("======结束 2======");
    }
}

package com.tuyrk.blog.controller;

import com.tuyrk.blog.domain.es.EsBlog;
import com.tuyrk.blog.repository.EsBlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Blog 控制器
 *
 * @Author : TYK tuyk涂元坤
 * @Mail : 766564616@qq.com
 * @Create : 2019/8/22 18:17 星期四
 * @Update : 2019/8/22 18:17 by tuyk涂元坤
 */
@RestController
@RequestMapping("/blogs")
public class BlogController {
    @Autowired
    private EsBlogRepository esBlogRepository;

    /**
     * http://localhost:8080/blogs?title=思&summary=相思&content=相思
     *
     * @param title 文章标题
     * @param summary 文章摘要
     * @param content 文章内容
     * @param pageIndex 分页当前页码
     * @param pageSize 分页当前数量
     * @return
     */
    @GetMapping
    public List<EsBlog> list(@RequestParam(value = "title") String title,
                             @RequestParam(value = "summary") String summary,
                             @RequestParam(value = "content") String content,
                             @RequestParam(value = "pageIndex", defaultValue = "0") Integer pageIndex,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        // 数据是在 Test 初始化的。
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<EsBlog> page = esBlogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining(title, summary, content, pageable);
        return page.getContent();
    }
}

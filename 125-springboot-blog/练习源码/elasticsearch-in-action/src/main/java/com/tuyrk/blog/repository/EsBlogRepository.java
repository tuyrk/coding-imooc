package com.tuyrk.blog.repository;

import com.tuyrk.blog.domain.es.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * EsBlogRepository 接口。
 *
 * @Author : TYK tuyk涂元坤
 * @Mail : 766564616@qq.com
 * @Create : 2019/8/22 17:04 星期四
 * @Update : 2019/8/22 17:04 by tuyk涂元坤
 */
public interface EsBlogRepository extends ElasticsearchRepository<EsBlog, String> {
    /**
     * 分页查询博客（去重）
     *
     * @param title
     * @param summary
     * @param content
     * @param pageable
     * @return
     */
    Page<EsBlog> findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContaining(String title, String summary, String content, Pageable pageable);
}

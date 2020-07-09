package com.tuyrk.blog.domain.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * EsBlog 文档。
 *
 * @Author : TYK tuyk涂元坤
 * @Mail : 766564616@qq.com
 * @Create : 2019/8/22 15:38 星期四
 * @Update : 2019/8/22 15:38 by tuyk涂元坤
 */
@Data
@Document(indexName = "blog", type = "blog") // 文档
/*
@Document(indexName = "blog", type = "blog", shards = 1, replicas = 0, refreshInterval = "-1")
@XmlRootElement // MediaType 转为 XML
 */
public class EsBlog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id  // 主键
    private String id; // 用户的唯一标识

    private String title;

    private String summary;

    private String content;

    protected EsBlog() { // JPA 的规范要求无参构造函数；设为 protected 防止直接使用
    }

    public EsBlog(String title, String summary, String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }
}

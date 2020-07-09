package com.tuyrk.sell.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/9 15:20 星期三
 * Description:
 * 类目
 */
@Data
@Entity
@DynamicUpdate
@Table(name = "product_category")
public class ProductCategory {
    /*类目ID*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
    /*类目名字*/
    private String categoryName;
    /*类目编号*/
    private Integer categoryType;
    private Date createTime;
    private Date updateTime;
    public ProductCategory() {
    }
    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }
}

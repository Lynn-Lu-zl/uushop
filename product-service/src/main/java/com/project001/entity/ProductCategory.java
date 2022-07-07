package com.project001.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 类目表
 * </p>
 *
 * @author admin
 * @since 2021-10-23
 */
//第二层数据，name，type实体类，储存数据库中的第二层信息的数据
@Data
  @EqualsAndHashCode(callSuper = false)
    public class ProductCategory implements Serializable {

    private static final long serialVersionUID=1L;//序列化，可不写

      @TableId(value = "category_id", type = IdType.AUTO)
      private Integer categoryId;

      /**
     * 类目名称
     */
      private String categoryName;

      /**
     * 类目编号
     */
      private Integer categoryType;

      /**
     * 创建时间
     */
        @TableField(fill = FieldFill.INSERT)
      private LocalDateTime createTime;

      /**
     * 修改时间
     */
        @TableField(fill = FieldFill.INSERT_UPDATE)
      private LocalDateTime updateTime;


}

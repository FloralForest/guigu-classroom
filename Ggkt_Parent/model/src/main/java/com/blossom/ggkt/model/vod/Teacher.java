package com.blossom.ggkt.model.vod;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.blossom.ggkt.model.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
@ApiModel(description = "Teacher")//描述名为Teacher的类
@TableName("teacher")//库表名
public class Teacher extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "讲师姓名")
	@TableField("name")
	private String name;

	@ApiModelProperty(value = "讲师简介")
	@TableField("intro")
	private String intro;

	@ApiModelProperty(value = "讲师资历,一句话说明讲师")
	@TableField("career")
	private String career;

	@ApiModelProperty(value = "头衔 1高级讲师 0首席讲师")
	@TableField("level")
	private Integer level;

	@ApiModelProperty(value = "讲师头像")
	@TableField("avatar")
	private String avatar;

	@ApiModelProperty(value = "排序")
	@TableField("sort")
	private Integer sort;

	@ApiModelProperty(value = "入驻时间")
	@JsonFormat(pattern = "yyyy-MM-dd")
	@TableField("join_date")
	private Date joinDate;

}
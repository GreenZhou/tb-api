package com.augurit.tb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "附件")
@Data
@TableName("tb_file")
public class TbFileInfo {
    private String id;
    private String suffix;
    private String originalName;
    private String status;
    private Date createTime;
    private String dirPath;
    private Long creatorId;
    private String creatorName;

}

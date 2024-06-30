package com.blossom.ggkt.vod.mapper;

import com.blossom.ggkt.model.vod.Subject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 课程科目 Mapper 接口
 * </p>
 *
 * @author blossom
 * @since 2023-05-04
 */
@Repository//它用于将数据访问层 (DAO 层 ) 的类标识为 Spring Bean
public interface SubjectMapper extends BaseMapper<Subject> {

}

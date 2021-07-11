package com.tuyrk.activiti7.mapper;


import com.tuyrk.activiti7.pojo.Act_ru_task;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;


/**
 * @author tuyrk@qq.com
 */
@Mapper
@Component
public interface ActivitiMapper {

    /**
     * 读取表单
     *
     * @param procInstId 流程实例ID
     * @return 本实例所有保存的表单数据
     */
    @Select("SELECT Control_ID_, Control_VALUE_ FROM formdata WHERE PROC_INST_ID_ = #{procInstId}")
    List<HashMap<String, Object>> selectFormData(@Param("procInstId") String procInstId);


    /**
     * 写入表单
     *
     * @param formDataList 动态表单数据
     * @return 影响行数
     */
    @Insert("<script>" +
            "    INSERT INTO formdata (PROC_DEF_ID_, PROC_INST_ID_, FORM_KEY_, Control_ID_, Control_VALUE_)" +
            "    VALUES" +
            "    <foreach collection=\"formDataList\" item=\"formData\" INDEX=\"index\" SEPARATOR=\",\">" +
            "        (#{formData.PROC_DEF_ID_,jdbcType=VARCHAR}," +
            "        #{formData.PROC_INST_ID_,jdbcType=VARCHAR}," +
            "        #{formData.FORM_KEY_,jdbcType=VARCHAR}," +
            "        #{formData.Control_ID_,jdbcType=VARCHAR}," +
            "        #{formData.Control_VALUE_,jdbcType=VARCHAR})" +
            "    </foreach>" +
            "</script>")
    int insertFormData(@Param("formDataList") List<HashMap<String, Object>> formDataList);

    //删除表单
    @Delete("DELETE FROM formdata WHERE PROC_DEF_ID_ = #{PROC_DEF_ID}")
    int DeleteFormData(@Param("PROC_DEF_ID") String PROC_DEF_ID);

    /**
     * 获取用户列表
     *
     * @return 姓名, 账号
     */
    @Select("SELECT name, username FROM user")
    List<HashMap<String, Object>> selectUser();

    //测试
    @Select("SELECT NAME_, TASK_DEF_KEY_ FROM ACT_RU_TASK")
    List<Act_ru_task> selectName();

    //流程定义数
    //SELECT COUNT(ID_) from ACT_RE_PROCDEF

    //进行中的流程实例
    //SELECT COUNT(DISTINCT PROC_INST_ID_) from ACT_RU_EXECUTION

    //查询流程定义产生的流程实例数
/*    SELECT p.NAME_,COUNT(DISTINCT e.PROC_INST_ID_) as PiNUM from act_ru_execution AS e
    RIGHT JOIN ACT_RE_PROCDEF AS p on e.PROC_DEF_ID_ = p.ID_
    WHERE p.NAME_ IS NOT NULL GROUP BY p.NAME_*/


}

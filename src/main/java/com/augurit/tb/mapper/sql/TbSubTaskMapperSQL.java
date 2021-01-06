package com.augurit.tb.mapper.sql;

import com.augurit.tb.entity.TbBuyer;
import com.augurit.tb.entity.TbSubTask;
import com.augurit.tb.entity.TbSubTaskEmp;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.util.Strings;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TbSubTaskMapperSQL {

    public String listSubTasks(@Param("pid") String pid){
        StringBuilder sb = new StringBuilder();
        sb.append("select id, pid, name subTaskName, order_num, buyer_need_num, buyer_num from tb_sub_task");
        if(!Strings.isBlank(pid)) {
            sb.append(" where pid = #{pid}");
        }
        return sb.toString();
    }

    public String saveSubTask(TbSubTask subTask) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into tb_sub_task(id, pid, name) values(#{id}, #{pid}, #{subTaskName})");
        return sb.toString();
    }

    public String updateSubTaskByImportOrders(TbSubTask subTask) {
        StringBuilder sb = new StringBuilder();
        sb.append("update tb_sub_task set order_num = #{orderNum}, buyer_need_num = #{buyerNeedNum} where id = #{id}");
        return sb.toString();
    }

    public String listEmps(@Param("creatorId") Long creatorId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select a.user_id, a.username, a.nickname, a.mobile, coalesce(b.check_status, '0') status");
        sb.append(" from sys_user a left outer join tb_sub_task_emp b");
        sb.append(" on a.user_id = b.emp_id");
        sb.append(" where a.status = '1'");
        return sb.toString();
    }

    public String assignEmps(Map<String, Object> subTaskEmpMap) {
        List<TbSubTaskEmp> subTaskEmps = (List<TbSubTaskEmp>) subTaskEmpMap.get("list");

        StringBuilder sb = new StringBuilder();
        sb.append("insert into tb_sub_task_emp(id, tsid, emp_id, emp_name, check_status) values ");

        String sqlValues = subTaskEmps.stream().map(subTaskEmp -> {
            StringBuilder buf = new StringBuilder();
            buf.append("(");
            buf.append("'" + subTaskEmp.getId() + "',");
            buf.append("'" + subTaskEmp.getTsid() + "',");
            buf.append(subTaskEmp.getEmpId() + ",");
            buf.append("'" + subTaskEmp.getEmpName() + "',");
            buf.append("'" + subTaskEmp.getCheckStatus() + "'");
            buf.append(")");

            return buf.toString();
        }).collect(Collectors.joining(","));

        sb.append(sqlValues);
        return sb.toString();
    }

    public String listSubTaskEmps(@Param("tsid") String tsid) {
        StringBuilder sb = new StringBuilder();
        sb.append("select id, tsid, emp_id, emp_name, coalesce(check_status, '0') check_status");
        sb.append(" from tb_sub_task_emp");
        if(Strings.isNotBlank(tsid)) {
            sb.append(" where tsid = #{tsid}");
        }
        return sb.toString();
    }

    public String removeAssignedEmps(@Param("tsid") String tsid) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from tb_sub_task_emp");
        sb.append(" where tsid = #{tsid}");
        return sb.toString();
    }

    public String listBuyers(@Param("tsid") String tsid, @Param("userId") Long userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select id, tsid, buyer_ww_name buyerWWName, status, byj");
        sb.append(" from tb_buyer");
        sb.append(" where tsid = #{tsid} and creator_id = #{userId}");
        return sb.toString();
    }

    public String removeBuyers(@Param("tsid") String tsid, Long userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from tb_buyer");
        sb.append(" where tsid = #{tsid} and creator_id = #{userId}");
        return sb.toString();
    }

    public String saveBuyerBatch(Map<String, Object> buyerMap) {
        List<TbBuyer> buyers = (List<TbBuyer>) buyerMap.get("list");

        StringBuilder sb = new StringBuilder();
        sb.append("insert into tb_buyer(id, tsid, buyer_ww_name, byj, creator_id) values ");

        String sqlValues = buyers.stream().map(buyer -> {
            StringBuilder buf = new StringBuilder();
            buf.append("(");
            buf.append("'" + buyer.getId() + "',");
            buf.append("'" + buyer.getTsid() + "',");
            buf.append("'" + buyer.getBuyerWWName() + "',");
            buf.append(buyer.getByj() + ",");
            buf.append("'" + buyer.getCreatorId() + "'");
            buf.append(")");

            return buf.toString();
        }).collect(Collectors.joining(","));

        sb.append(sqlValues);
        return sb.toString();
    }

    public String listEmpSubTasks(@Param("tsid") String tsid, @Param("userId") Long userId, @Param("status") String status) {
        StringBuilder sb = new StringBuilder();
        sb.append("select a.id, concat(b.name, '-', a.name) subTaskName, a.order_num, a.buyer_need_num, a.buyer_num");
        sb.append(" from tb_sub_task a left join tb_task b on a.pid = b.id");
        sb.append(" where a.id in ( select tsid from tb_sub_task_emp where emp_id = #{userId} )");
        sb.append(" and b.status = #{status}");
        if(StringUtils.isNotBlank(tsid)) {
            sb.append(" and a.id = #{tsid}");
        }
        return sb.toString();
    }

    public String deleteBuyers(@Param("userId") Long userId, @Param("tsid") String tsid) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from tb_buyer");
        sb.append(" where tsid = #{tsid} and creator_id = #{userId}");
        return sb.toString();
    }

    public String updateSubTaskEmp(TbSubTaskEmp tbSubTaskEmp) {
        StringBuilder sb = new StringBuilder();
        sb.append("update tb_sub_task_emp");
        sb.append(" set check_status = #{checkStatus}");
        sb.append(" where tsid = #{tsid} and emp_id = #{empId}");
        return sb.toString();
    }

    public String updateBuyer(TbBuyer buyer) {
        StringBuilder sb = new StringBuilder();
        sb.append("update tb_buyer");
        sb.append(" set status = #{status}");
        sb.append(" where tsid = #{tsid} and creator_id = #{creatorId}");
        return sb.toString();
    }
}

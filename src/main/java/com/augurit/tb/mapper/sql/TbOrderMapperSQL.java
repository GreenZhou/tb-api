package com.augurit.tb.mapper.sql;

import com.augurit.tb.entity.TbOrder;
import com.augurit.tb.entity.TbSubTask;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.util.Strings;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TbOrderMapperSQL {
    /*
     * 这里很坑，使用代码实现方式，mybatis对Dao中的list进行了封装
     */
    public String saveBatch(Map<String, Object> orderMap) {
        List<TbOrder> orders = (List<TbOrder>) orderMap.get("list");

        StringBuilder sb = new StringBuilder();
        sb.append("insert into tb_order(id, tsid, saler_name, saler_ww_name, price, num, purl, yj, keywd, demand, tprice) values ");

        String sqlValues = orders.stream().map(order -> {
            StringBuilder buf = new StringBuilder();
            buf.append("(");
            buf.append("'" + order.getId() + "',");
            buf.append("'" + order.getTsid() + "',");
            buf.append("'" + order.getSalerName() + "',");
            buf.append("'" + order.getSalerWWName() + "',");
            buf.append(order.getPrice() + ",");
            buf.append(order.getNum() + ",");
            buf.append("'" + order.getPurl() + "',");
            buf.append(order.getYj() + ",");
            buf.append("'" + order.getKeywd() + "',");
            buf.append("'" + order.getDemand() + "',");
            buf.append(order.getTprice());
            buf.append(")");

            return buf.toString();
        }).collect(Collectors.joining(","));

        sb.append(sqlValues);
        return sb.toString();
    }

    public String deleteOrders(@Param("taskId") String taskId) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete from tb_order where tsid in (select id from tb_sub_task where pid = #{taskId})");

        return sb.toString();
    }

    public String listSubTaskBuyerCount(@Param("taskId") String taskId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select tsid id, count(0) buyerNeedNum from tb_order");
        sb.append(" group by tsid, saler_ww_name having tsid in (select tsid from tb_task where id = #{taskId})");
        return sb.toString();
    }

    public String listOrders(@Param("taskId") String taskId, @Param("tsids") List<String> tsids) {
        StringBuilder sb = new StringBuilder();
        sb.append("select a.name subTaskName, b.saler_name, b.saler_ww_name salerWWName, b.keywd, b.demand, b.price, b.num, b.tprice");
        sb.append(" from tb_sub_task a left join tb_order b on a.id = b.tsid");
        sb.append(" where b.id is not null");
        if(Strings.isNotBlank(taskId)) {
            sb.append(" and a.pid = #{taskId}");
        }
        if(CollectionUtils.isNotEmpty(tsids)) {
            String subTaskIdSqlVals  = tsids.stream().map(subTaskId -> {
                return "'" + subTaskId + "'";
            }).collect(Collectors.joining(","));

            sb.append(" and a.id in (" + subTaskIdSqlVals + ")");
        }
        sb.append(" order by b.saler_name");

        return sb.toString();
    }

    public String listOrderBuyers(@Param("tsid") String tsid, @Param("userId") Long userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select  buyer_ww_name buyerWWName,");
        sb.append(" sum(case when check_status = '1' then 1 else 0 end) checkedOrderBuyerNum,");
        sb.append(" sum(case when check_status = '0' then 1 else 0 end) uncheckedOrderBuyerNum,");
        sb.append(" count(1) yxOrderBuyerNum, sum(tprice) tprice,");
        sb.append(" sum(case when xd_status = '1' then 1 else 0 end) xdOrderBuyerNum,");
        sb.append(" sum(tprice_sj) tpriceSj, max(byj) byj");
        sb.append(" from tb_order_buyer");
        sb.append(" where tsid = #{tsid}");
        sb.append(" group by buyer_ww_name");

        return sb.toString();
    }
}

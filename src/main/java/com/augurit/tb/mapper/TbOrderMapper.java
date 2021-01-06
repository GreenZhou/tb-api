package com.augurit.tb.mapper;

import com.augurit.tb.entity.TbOrder;
import com.augurit.tb.entity.TbOrderBuyer;
import com.augurit.tb.entity.TbSubTask;
import com.augurit.tb.mapper.sql.TbOrderMapperSQL;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface TbOrderMapper {
    @InsertProvider(type = TbOrderMapperSQL.class, method = "saveBatch")
    void saveBatch(List<TbOrder> orders);

    @DeleteProvider(type = TbOrderMapperSQL.class, method = "deleteOrders")
    void deleteOrders(String taskId);

    @SelectProvider(type = TbOrderMapperSQL.class, method = "listSubTaskBuyerCount")
    List<TbSubTask> listSubTaskBuyerCount(String taskId);

    @SelectProvider(type = TbOrderMapperSQL.class, method ="listOrders")
    List<TbOrder> listOrders(String taskId, List<String> tsids);

    @SelectProvider(type = TbOrderMapperSQL.class, method = "listOrderBuyers")
    List<TbOrderBuyer> listOrderBuyers(String tsid, Long userId);
}

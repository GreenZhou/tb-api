package com.augurit.tb.service;

import com.augurit.common.utils.DefaultIdGenerator;
import com.augurit.common.utils.PageResult;
import com.augurit.common.utils.QueryParam;
import com.augurit.tb.entity.TbBuyer;
import com.augurit.tb.entity.TbOrder;
import com.augurit.tb.entity.TbOrderBuyer;
import com.augurit.tb.entity.TbSubTask;
import com.augurit.tb.mapper.TbOrderMapper;
import com.augurit.tb.mapper.TbSubTaskMapper;
import com.augurit.tb.poi.TbOrderProcessor;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

@Service("tbOrderService")
public class TbOrderService {

    public static final String EMP_STATUS_CHECKED = "2";

    @Autowired
    private TbOrderMapper mapper;

    @Autowired
    private TbSubTaskMapper tbSubTaskMapper;

    public void saveBatch(List<TbOrder> orders, String taskId) {
        // 1、移除所有order订单
        mapper.deleteOrders(taskId);

        // 2、批量保存订单信息
        mapper.saveBatch(orders);

        // 3、更新subTask的订单数和需要买家数
        Map<String, Integer> orderNumMap = Maps.newHashMap();
        Map<String, Integer> buyerNeedNumMap = Maps.newHashMap();

        List<TbSubTask> subTaskCounts = mapper.listSubTaskBuyerCount(taskId);
        subTaskCounts.forEach(subTaskCount -> {
            String tsid = subTaskCount.getId();

            if(orderNumMap.get(tsid) == null) {
                orderNumMap.put(tsid, 0);
            }

            if(buyerNeedNumMap.get(tsid) == null) {
                buyerNeedNumMap.put(tsid, 0);
            }

            int orderNum = orderNumMap.get(tsid);
            int buyerNeedNum = buyerNeedNumMap.get(tsid);

            orderNumMap.put(tsid, orderNum + subTaskCount.getBuyerNeedNum());
            if(buyerNeedNum < subTaskCount.getBuyerNeedNum()) {
                buyerNeedNumMap.put(tsid, subTaskCount.getBuyerNeedNum());
            }
        });

        orderNumMap.forEach((tsid, orderNum) -> {
            TbSubTask subTask = new TbSubTask();
            subTask.setId(tsid);
            subTask.setOrderNum(orderNum);
            subTask.setBuyerNeedNum(buyerNeedNumMap.get(tsid));

            tbSubTaskMapper.updateSubTaskByImportOrders(subTask);
        });
    }

    public PageResult page(String taskId, List<String> tsids, QueryParam queryParam) {
        PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize(), queryParam.getOrderBy());
        List<TbOrder> orders = mapper.listOrders(taskId, tsids);
        return new PageResult(orders);
    }

    public PageResult listOrderBuyersPage(String tsid, Long userId, QueryParam queryParam) {
        PageHelper.startPage(queryParam.getPageNum(), queryParam.getPageSize(), queryParam.getOrderBy());
        List<TbOrderBuyer> orderBuyers = mapper.listOrderBuyers(tsid, userId);
        return new PageResult(orderBuyers);
    }

    public void generateOrderBuyerExcel(String tsid, Long userId, HttpServletResponse resp) throws Exception {
        // 1. 获取所有的买家
        List<TbBuyer> buyers = tbSubTaskMapper.listBuyers(tsid, userId);

        // 2. 获取所有的订单
        List<TbOrder> orders = mapper.listOrders(null, Lists.newArrayList(tsid));

        List<TbOrderBuyer> orderBuyers = Lists.newArrayList();
        int buyerIndex = 0;
        for(TbOrder order: orders) {
            TbOrderBuyer orderBuyer = new TbOrderBuyer();
            TbBuyer buyer = buyers.get(buyerIndex++/buyers.size());
            orderBuyer.setBuyerId(buyer.getId());
            orderBuyer.setSalerName(order.getSalerName());
            orderBuyer.setBuyerWWName(buyer.getBuyerWWName());
            orderBuyer.setByj(order.getByj());
            orderBuyer.setId(DefaultIdGenerator.getIdForStr());
            orderBuyer.setNum(order.getNum());
            orderBuyer.setNumSj(order.getNum());
            orderBuyer.setPrice(order.getPrice());
            orderBuyer.setPriceSj(order.getPrice());
            orderBuyer.setTprice(order.getTprice());
            orderBuyer.setTpriceSj(order.getTprice());
            orderBuyer.setOrderId(order.getId());
            orderBuyer.setTsid(order.getTsid());

            orderBuyers.add(orderBuyer);
        }

        // 3. 保存订单核查数据
        //mapper.saveOrderBuyerBatch(orderBuyers);

        // 4. 生成Excel文件
        TbOrderProcessor.generateOrderBuyerExcel(orderBuyers, buyers, resp);

        // 5. 生成文件流返回

    }
}

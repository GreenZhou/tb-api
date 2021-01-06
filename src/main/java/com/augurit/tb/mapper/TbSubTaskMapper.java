package com.augurit.tb.mapper;

import com.augurit.sys.entity.SysUser;
import com.augurit.tb.entity.TbBuyer;
import com.augurit.tb.entity.TbSubTask;
import com.augurit.tb.entity.TbSubTaskEmp;
import com.augurit.tb.mapper.sql.TbSubTaskMapperSQL;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TbSubTaskMapper {

    @SelectProvider(type = TbSubTaskMapperSQL.class, method ="listSubTasks")
    List<TbSubTask> listSubTasks(String pid);

    @InsertProvider(type = TbSubTaskMapperSQL.class, method = "saveSubTask")
    void saveSubTask(TbSubTask subTask);

    @InsertProvider(type = TbSubTaskMapperSQL.class, method = "updateSubTaskByImportOrders")
    void updateSubTaskByImportOrders(TbSubTask subTask);

    @SelectProvider(type = TbSubTaskMapperSQL.class, method ="listEmps")
    List<SysUser> listEmps(Long creatorId);

    @SelectProvider(type = TbSubTaskMapperSQL.class, method ="assignEmps")
    void assignEmps(List<TbSubTaskEmp> subTaskEmps);

    @SelectProvider(type = TbSubTaskMapperSQL.class, method ="listSubTaskEmps")
    List<TbSubTaskEmp> listSubTaskEmps(String tsid);

    @DeleteProvider(type = TbSubTaskMapperSQL.class, method ="removeAssignedEmps")
    void removeAssignedEmps(String tsid);

    @SelectProvider(type = TbSubTaskMapperSQL.class, method ="listBuyers")
    List<TbBuyer> listBuyers(String tsid, Long userId);

    @DeleteProvider(type = TbSubTaskMapperSQL.class, method ="removeBuyers")
    void removeBuyers(String tsid, Long userId);

    @InsertProvider(type = TbSubTaskMapperSQL.class, method ="saveBuyerBatch")
    void saveBuyerBatch(List<TbBuyer> buyers);

    @SelectProvider(type = TbSubTaskMapperSQL.class, method ="listEmpSubTasks")
    List<TbSubTask> listEmpSubTasks(String tsid, Long userId, String status);

    @DeleteProvider(type = TbSubTaskMapperSQL.class, method ="deleteBuyers")
    void deleteBuyers(String tsid, Long userId);

    @UpdateProvider(type = TbSubTaskMapperSQL.class, method ="updateSubTaskEmp")
    void updateSubTaskEmp(TbSubTaskEmp subTaskEmp);

    @UpdateProvider(type = TbSubTaskMapperSQL.class, method ="updateBuyer")
    void updateBuyer(TbBuyer buyer);
}

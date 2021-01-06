package com.augurit.tb.service;

import com.augurit.tb.entity.TbFileInfo;
import com.augurit.tb.mapper.TbFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tbFileService")
public class TbFileService {

    @Autowired
    private TbFileMapper mapper;

    /**
     * 保存附件
     * @param fileInfo 附件信息对象
     * @throws Exception
     */
    public void saveFile(TbFileInfo fileInfo) {
        mapper.saveFile(fileInfo);
    }

    /**
     * 更新附件状态
     * @param id 附件ID
     * @param status 0：无效 1：有效
     * @throws Exception
     */
    public void updateFileStatus(String id, String status) {
        mapper.updateFileStatus(id, status);
    }

    /**
     * 通过id组查找所有的附件信息
     * 注意： 如果ids插入空的集合，则查询所有附件信息，该方法不负责排序相关操作
     * @param ids
     * @return
     * @throws Exception
     */
    public List<TbFileInfo> listFileInfos(List<String> ids) {
        return mapper.listFileInfos(ids);
    }

    /**
     * 通过id查找单个文件信息对象
     * @param id
     * @return
     * @throws Exception
     */
    public TbFileInfo getFileInfo(String id) {
        return mapper.getFileInfo(id);
    }

    /**
     * 删除一组文件记录
     * @param ids 文件ID集合
     */
    public void removeFiles(List<String> ids) {
        mapper.removeFiles(ids);
    }
}

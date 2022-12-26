package com.nagisazz.base.dao.base;

import com.nagisazz.base.entity.FileInfo;
import java.util.List;

public interface FileInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FileInfo fileinfo);

    int insertSelective(FileInfo fileinfo);

    FileInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FileInfo fileinfo);

    int updateByPrimaryKey(FileInfo fileinfo);

    FileInfo selectOne(FileInfo fileinfo);

    List<FileInfo> selectList(FileInfo fileinfo);

    int deleteSelective(FileInfo fileinfo);

    int batchUpdate(List<FileInfo> fileinfoList);

    int batchUpdateSelective(List<FileInfo> fileinfoList);

    int batchInsert(List<FileInfo> fileinfoList);

    int batchDelete(List<Long> fileinfoList);

    List<FileInfo> batchSelect(List<Long> fileinfoList);
}
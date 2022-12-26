package com.nagisazz.base.dao;

import com.nagisazz.base.dao.base.FileInfoMapper;
import com.nagisazz.base.entity.FileInfo;
import com.nagisazz.base.entity.ZlpUser;

import java.util.List;

public interface FileInfoExtendMapper extends FileInfoMapper {

    void insertForId(FileInfo fileInfo);
}
import TextEditor from "@/components/textEditor";
import '@/views/shareFile.less';
import { Button, Checkbox, Input, Modal, message, Upload } from "antd";
import type { UploadProps } from 'antd';
import { useEffect, useMemo, useRef, useState } from "react";
import { ExclamationCircleOutlined } from '@ant-design/icons';
import {connect} from 'react-redux';
import { getFileList, createFolder, updateFolder, deleteFolder, singleShareCode,
    multiShareCode, previewShare, saveShareFile, updateFile, uploadFile,
    createFile, getFile, getPreviewFile 
} from '@/api/index';
import actions from '@/qiankun/action';


const { confirm } = Modal;
const useClick = (callback: any, doubleCallback: any) => {
    let timer: any;
    const clickRef = useRef({
      clickCount: 0,
      time: 0,
    });
    return (...args: any) => {
      clickRef.current.clickCount += 1;
      clickRef.current.time = Date.now();
      timer = setTimeout(() => {
        if (
          Date.now() - clickRef.current.time <= 200 &&
          clickRef.current.clickCount === 2
        ) {
          doubleCallback && doubleCallback.apply(null, args);
        }
        if (clickRef.current.clickCount === 1) {
          callback && callback.apply(null, args);
        }
        clearTimeout(timer);
        clickRef.current.clickCount = 0;
      }, 200);
    };
};

const ShareFile = (props: any) => {
    const { loginInfo } = props; 
    const [searchInfo, setSearchInfo] = useState(''); // 关键词
    const [showType, setShowType] = useState('thumbnail'); // thumbnail缩略图 list列表
    const newShowType = useRef(showType);
    const [fileList, setFileList] = useState([ // 文件列表
        {
            title: '生活照',
            file_type: 'folder',
            id: '1',
            file_icon: 'zp-wenjianjia',
            createTime: '2022/11/26  11:37',
            size: '230kb',
            selected: false,
            rename: false,
        },
        {
            title: '平平经历的视频',
            file_type: 'video',
            id: '2',
            file_icon: 'zp-wenjianleixing-biaozhuntu-shipinwenjian',
            createTime: '2022/11/26  11:37',
            size: '230kb',
            selected: false,
            rename: false,
        },
        {
            title: '平平经历的文档',
            file_type: 'note',
            id: '3',
            file_icon: 'zp-wendang',
            createTime: '2022/11/26  11:37',
            size: '230kb',
            selected: false,
            rename: false,
        },
        {
            title: '生活照',
            file_type: 'folder',
            id: '4',
            file_icon: 'zp-wenjianjia',
            createTime: '2022/11/26  11:37',
            size: '230kb',
            selected: false,
            rename: false,
        },
        {
            title: '平平经历的视频',
            file_type: 'file',
            id: '5',
            file_icon: 'zp-wenjianleixing-biaozhuntu-shipinwenjian',
            createTime: '2022/11/26  11:37',
            size: '230kb',
            selected: false,
            rename: false
        },
        {
            title: '平平经历的文档',
            file_type: 'file',
            id: '6',
            file_icon: 'zp-wendang',
            createTime: '2022/11/26  11:37',
            size: '230kb',
            selected: false,
            rename: false
        },
        {
            title: '生活照',
            file_type: 'folder',
            id: '7',
            file_icon: 'zp-wenjianjia',
            createTime: '2022/11/26  11:37',
            size: '230kb',
            selected: false,
            rename: false
        },
        {
            title: '平平经历的视频',
            file_type: 'file',
            id: '8',
            file_icon: 'zp-wenjianleixing-biaozhuntu-shipinwenjian',
            createTime: '2022/11/26  11:37',
            size: '230kb',
            selected: false,
            rename: false
        },
        {
            title: '平平经历的文档平平经历的文档平平经历的文档平平经历的文档平平经历的文档平平经历的文档',
            file_type: 'file',
            id: '9',
            file_icon: 'zp-wendang',
            createTime: '2022/11/26  11:37',
            size: '230kb',
            selected: false,
            rename: false
        },
        {
            title: '生活照',
            file_type: 'folder',
            id: '10',
            file_icon: 'zp-wenjianjia',
            createTime: '2022/11/26  11:37',
            size: '230kb',
            selected: false,
            rename: false,
        },
        {
            title: '生活照',
            file_type: 'folder',
            id: '11',
            file_icon: 'zp-wenjianjia',
            createTime: '2022/11/26  11:37',
            size: '230kb',
            selected: false,
            rename: false,
            children: [{
                title: '平平经历的文档平平经历的文档',
                file_type: 'file',
                id: '111',
                file_icon: 'zp-wendang',
                createTime: '2022/11/26  11:37',
                size: '230kb',
                selected: false,
            }]
        },
    ] as any);
    const newFileList = useRef(fileList);
    const [currentFile, setCurrentFile] = useState(''); // 当前所选的文件id
    const newCurrentFile = useRef(currentFile);
    const [currentFileData, setCurrentFileData] = useState({} as any); // 当前所选的文件内容
    const newCurrentFileData = useRef(currentFileData);
    const [rightClickFile, setRightClickFile] = useState(''); // 右键所选的文件id
    const newRightClickFile = useRef(rightClickFile);
    const [allSelected, setAllSelected] = useState(false); // 是否全选
    const newAllSelected = useRef(allSelected);

    const [shareModalOpen, setShareModalOpen] = useState(false); // 打开分享码开窗
    const [shareCode, setShareCode] = useState(''); // 分享码

    const [codeModalOpen, setCodeModalOpen] = useState(false); // 打开解析分享码开窗
    const [analysisCode, setAnalysisCode] = useState(''); // 输入的分享码

    const [noteModalOpen, setNoteModalOpen] = useState(false); // 打开笔记开窗
    const [fileCatalogueList, setFileCatalogueList] = useState([{txt: '我的文件', id: '0'}]); // 打开的文件目录
    const newFileCatalogueList = useRef(fileCatalogueList);

    // 监听文件列表中的selected字段
    const isHaveSelect = useMemo(() => {
            if (newFileList && newFileList.current && newFileList.current.length > 0) {
                return newFileList.current.filter((obj: any) => {
                    return obj.selected;
                })
            } else {
                return [];
            }
        },
        [fileList]
    );

    useEffect(() => {
        // 取消双击时文字选择功能
        document.onselectstart = function(){return false;};
        console.log('init');
        getList();
    }, [])

    // 获取文件列表
    const getList = async () => {
        const parentId = newFileCatalogueList.current[newFileCatalogueList.current.length - 1].id; // 顶层目录传0
        // order: '2', // 1创建时间 2名称
        const { data, status } = await getFileList('parentId=' + parentId + '&order=2');
        if (status === 200) {
            data.map((obj: any) => {
                obj.selected = false;
                obj.rename = false;
                obj.file_type = dealTypeStr(obj.type).type;
                obj.file_icon = dealTypeStr(obj.type).icon;
                obj.file_txt = dealTypeStr(obj.type).txt;
                obj.size = obj.size ? sizeTostr(obj.size) : '-';
            })
            newFileList.current = data;
            setFileList([...newFileList.current]);
        }
        
    };
    
    // 关键字赋值
    const setSearchVal = (e: any) => {
        setSearchInfo(e.target.value);
    }

    // 清空关键字
    const clearInfo = () => {
        setSearchInfo('');
        onSearch();
    }

    // 关键词enter事件
    const onEnter = (e: any) => {
        onSearch(e.target.value);
    }

    // 查询
    const onSearch = async (val?: any) => {

    }

    // 显示缩列图还是列表
    const changeShow = (show: string) => {
        newShowType.current = show;
        setShowType(newShowType.current);
    }

    // list显示时摔鼠标悬浮显示操作按钮
    const handleMouseOver = (file: any) => {
        newCurrentFile.current = file.id;
        setCurrentFile(newCurrentFile.current);
    }
    
    // list显示时摔鼠标悬浮离开
    const handleMouseLeave = (file: any) => {
        newCurrentFile.current = '';
        setCurrentFile(newCurrentFile.current);
    }

    // 单个勾选事件
    const selectFileChange = (file: any) => {
        newFileList.current.map((obj: any) => {
            if (obj.id === file.id) {
                obj.selected = !obj.selected;
            }
        })
        setFileList([...newFileList.current]);
    }

    // 全选,全不选事件
    const selectAllFile = () => {
        newAllSelected.current = !newAllSelected.current;
        setAllSelected(newAllSelected.current);
        newFileList.current.map((obj: any) => {
            obj.selected = newAllSelected.current;
        })
        setFileList([...newFileList.current]);
    }

    // 删除，批量删除
    const onDel = (file?: any) => {
        confirm({
            title: '系统提示',
            icon: <ExclamationCircleOutlined />,
            content: '确定要删除所选文件吗？',
            cancelText: '取消',
            okText: '确定',
            async onOk() {
                // 删除接口
                let delList = [];
                if (file) {
                    delList = newFileList.current.filter((obj: any) => {
                        return obj.id === file.id;
                    })
                } else {
                    delList = newFileList.current.filter((obj: any) => {
                        return obj.selected;
                    })
                }
                const delIds = delList.map((del: any) => {
                    return del.id;
                }) || [];
                const param = {
                    ids: delIds
                };
                const { status } = await deleteFolder(param);
                if (status === 200) {
                    getList();
                }
            },
        });
    }

    // 重命名
    const onReName = (file: any) => {
        newFileList.current.map((obj: any) => {
            if (obj.id === file.id) {
                obj.rename = true;
                setTimeout(() => {
                    document.getElementById('input' + file.id)?.focus(); 
                }, 100);
            }
        })
        setFileList([...newFileList.current]);
    }

    // 重命名enter事件
    const onFileEnter = async(file: any) => {
        if (file.file_type === 'folder') {
            const param = {
                parentId: fileCatalogueList[fileCatalogueList.length - 1].id,
                title: file.title,
                id: file.id,
            }
            const { status } = await updateFolder(param);
            updateFileList(status, file);
        } else {
            const param = {
                content: file.content,
                title: file.title,
                id: file.id,
            };
            const { status } = await updateFile(param);
            updateFileList(status, file);
        }
    }

    const updateFileList = (status: any, file: any) => {
        if (status === 200) {
            getList();
        } else {
            newFileList.current.map((obj: any) => {
                if (obj.id === file.id) {
                    obj.rename = false;
                }
            })
            setFileList([...newFileList.current]);
        }
    }

    // 重命名获取新内容
    const setFileName = (e: any) => {
        newFileList.current.map((obj: any) => {
            if (obj.rename) {
                obj.title = e.target.value;
            }
        })
        setFileList([...newFileList.current]);
    }

    // 分享，批量分享
    const onShare = (file?: any) => {
        setShareCode('');
        setShareModalOpen(true);
        if (!file) {
            newRightClickFile.current = '';
            setRightClickFile(newRightClickFile.current);
        }
    }

    // 分享码开窗关闭事件
    const shareModalCancel = () => {
        setShareModalOpen(false);
        newFileList.current.map((obj: any) => {
            obj.selected = false;
        });
        setFileList([...newFileList.current]);
    }

    // 生成分享码
    const generateShareCode = async () => {
        // 调用方法
        if (rightClickFile) { // 单个获取分享码
            const param = {
                fileObjId: rightClickFile,
                title: '分享',
                limitDate: '7'
            };
            const { status, data } = await singleShareCode(param);
            if (status === 200) {
                setShareCode(data);
            }
        } else { // 多文件获取分享码
            const shareList = newFileList.current.filter((obj: any) => {
                return obj.selected;
            })
            const shareIds = shareList.map((obj: any) => {
                return obj.id;
            })
            const param = {
                fileObjIds: shareIds,
                title: '分享',
                limitDate: '7'
            };
            const { status, data } = await multiShareCode(param);
            if (status === 200) {
                setShareCode(data);
            }
        }
    }

    // 解析分享码开窗打开事件
    const analysisFile = () => {
        setCodeModalOpen(true);
    }

    // 解析分享码开窗关闭事件
    const codeModalCancel = () => {
        setCodeModalOpen(false);
    }

    // 解析码赋值
    const onAnalysisCode = (e: any) => {
        setAnalysisCode(e.target.value);
    }

    // 解析分享码
    const analysisShareCode = async () => {
        const param = {
            shareCode: analysisCode
        }
        const { status } = await previewShare(param);
        if (status === 200) {
        }
    }

    // 保存分享的文件
    const saveShareCodeFile = async () => {
        const param = {
            shareCode: analysisCode
        }
        const { status } = await saveShareFile(param);
        if (status === 200) {
        }
    }

    // 新建文件夹
    const addFolder = async () => {
        if (!checkIsLogin()) {
            return;
        }
        const param = {
            parentId: fileCatalogueList[fileCatalogueList.length - 1].id,
            title: '新建文件夹'
        };
        const { status } = await createFolder(param);
        if (status === 200) {
            getList();
        }
    }

    // 新建笔记
    const addNote = () => {
        if (!checkIsLogin()) {
            return;
        }
        newCurrentFileData.current = {};
        setCurrentFileData(newCurrentFileData.current);
        setNoteModalOpen(true);
    }

    // 笔记开窗关闭事件
    const noteModalCancel = () => {
        setNoteModalOpen(false);
        setTimeout(() => {
            getList();
        }, 200);
    }

    const onLabelClick = useClick(
        (file: any) => {
          console.log('单击事件触发了');
        },
        async(file: any) => {
            if (file.file_type === 'folder') {
                newFileCatalogueList.current.push({ txt: file.title, id: file.id });
                setFileCatalogueList([...newFileCatalogueList.current]);
                getList();
            } else if (file.file_type === 'note') { // 笔记双击进入富文本编辑器
                addNote();

                const data = fileList.filter((obj: any) => {
                    return obj.id === currentFile;
                });
                newCurrentFileData.current = data ? data[0] : {};
                setCurrentFileData(newCurrentFileData.current);
            } else if (file.file_type === 'file' || file.file_type === 'img') { // 图片
                const { status, data } = await getFile('systemId=share&fileId=' + file.fileId);
                if (status === 200) {

                }
            }
            // 查询双击文件夹的下级目录
          console.log('双击事件触发了', file.file_type, fileCatalogueList, newCurrentFileData.current);
        }
    );

    // 目录的点击事件
    const onClickCatalogue = (catalogue: any) => {
        // 查询点击目录的下级列表+判断前进后退
        const idx = newFileCatalogueList.current.findIndex((obj) => {
            return obj.id === catalogue.id;
        });
        console.log(idx);
        newFileCatalogueList.current.splice(idx + 1);
        setFileCatalogueList([...newFileCatalogueList.current]);
        getList();
    }

    // 目录回退一步
    const goBack = () => {
        if (newFileCatalogueList.current.length > 1) {
            newFileCatalogueList.current.pop();
            setFileCatalogueList([...newFileCatalogueList.current]);
        }
    }

    // 目录前进一步
    const goIn = () => {
        if (newFileCatalogueList.current.length > 1) {
            newFileCatalogueList.current.pop();
            setFileCatalogueList([...newFileCatalogueList.current]);
        }
    }

    // 右击事件
    const onRightClickMenu = (id: any, e: any) => {
        e.preventDefault()
        // 此条件下才展示弹窗
        newRightClickFile.current = id;
        setRightClickFile(newRightClickFile.current);
        const x = e.clientX
        const y = e.clientY
        // 弹窗节点
        const rightMenu = document.getElementsByClassName("right-menu")[0] as HTMLElement;
        rightMenu.style.display = "block"
        rightMenu.style.left = x + 10 + "px" //设置弹窗位置
        rightMenu.style.top = y + "px"
        document.onclick = function () { //点击其他区域要隐藏弹窗
            rightMenu.style.display = "none"
        }
    }

    // 设置右击出现的按钮
    const getRightMenuBtns = () => {
        const btns = [
          {
            text: "重命名",
            key: "rename",
            handleClick: handleCloseTab,
          },
          {
            text: "分享",
            key: "share",
            handleClick: handleCloseTab,
          },
          {
            text: "删除",
            key: "del",
            handleClick: handleCloseTab,
          },
        ]
        const res = btns.map((item) => {
          return (
            <li onClick={() => item.handleClick(item.key)} key={item.key}>
              {item.text}
            </li>
          )
        })
        return [...res];
    }

    // 右击按钮的点击事件
    const handleCloseTab = (key: string) => {
        const file = fileList.filter((obj: any) => {
            return obj.id == rightClickFile;
        })[0];
        console.log(file, rightClickFile);
        if (key === 'rename') {
            onReName(file);
        } else if (key === 'share') {
            onShare(file);
        } else if (key === 'del') {
            onDel(file);
        }
    }

    // 拷贝分享码
    const onCopy = () => {
        const range = document.createRange();
        range.selectNode(document.getElementById('sharecode') as HTMLElement);
        const selection = window.getSelection();
        if(selection && selection.rangeCount > 0) {
            selection.removeAllRanges();
            selection.addRange(range);
            document.execCommand('copy');
        }
    }

    // 处理时间显示
    const dealFormate = (time: any) => {
        return time.replace('T', ' ');
    }

    // 处理文件类型转换
    const dealTypeStr = (type: any) => {
        // 类型（100：根文件夹，101：文件夹，102：协作根文件夹，
        // 1：文本，2：文档，3：图片，4：视频，5：音频，6：其他）
        if (type === 100 || type === 101 || type === 102) {
            return { type: 'folder', icon: 'zp-wenjianjia', txt: '文件夹' };
        } else if (type === 3) {
            return { type: 'img', icon: '', txt: '图片' };
        } else if (type === 4) {
            return { type: 'file', icon: 'zp-wenjianleixing-biaozhuntu-shipinwenjian', txt: '视频' };
        } else if (type === 5) {
            return { type: 'file', icon: 'zp-wenjianleixing-biaozhuntu-shipinwenjian', txt: '音频' };
        } else if (type === 1) {
            return { type: 'note', icon: 'zp-wendang', txt: '笔记' };
        } else {
            return { type: 'file', icon: 'zp-wendang', txt: '文件' };
        }
    }

    // 上传文件类型转换
    const dealUploadType = (type: any) => {
        const types = type.split('/');
        console.log(types);
        if (types[0] === 'image') {
            return 3;
        } else if (types[0] === 'application' || types[0] === 'text') {
            return 2;
        } else if (types[0] === 'video') {
            return 4;
        } else if (types[0] === 'audio') {
            return 5;
        } else {
            return 6;
        }
    }

    const sizeTostr = (size: any) => {
        let data = '';
        if (size < 0.1 * 1024) { //如果小于0.1KB转化成B  
            data = size.toFixed(2) + "B";
        } else if (size < 0.1 * 1024 * 1024) {//如果小于0.1MB转化成KB  
            data = (size / 1024).toFixed(2) + "KB";
        } else if (size < 0.1 * 1024 * 1024 * 1024) { //如果小于0.1GB转化成MB  
            data = (size / (1024 * 1024)).toFixed(2) + "MB";
        } else { //其他转化成GB  
            data = (size / (1024 * 1024 * 1024)).toFixed(2) + "GB";
        }
        const sizestr = data + "";
        const len = sizestr.indexOf("\.");
        const dec = sizestr.substr(len + 1, 2);
        if (dec == "00") {//当小数点后为00时 去掉小数部分  
            return sizestr.substring(0, len) + sizestr.substr(len + 3, 2);
        }
        return sizestr;
    }

    // 处理文件上传
    const handleChange = async(info: any) => {
        if (info.file.status !== 'uploading') {
            // console.log(info.file, info.fileList);
        }
        const fileDone = info.fileList.every((obj: any) => {
            return obj.status === 'done';
        })
        if (fileDone) {
            console.log(info);
            info.fileList.map(async(file: any) => {
                const formData = new FormData();
                formData.append('file', file.originFileObj);
                const { status, data } = await uploadFile(formData, 'systemId=share');
                if (status === 200) {
                    const param = {
                        title: file.name,
                        fileId: data.id,
                        parentId: fileCatalogueList[fileCatalogueList.length - 1].id,
                        fileType: dealUploadType(file.type),
                        size: data.size
                    };
                    const { status } = await createFile(param);
                    if (status === 200) {
                        message.success(`${info.file.name} 上传成功`);
                        getList();
                    }
                }
            })
        } else if (info.file.status === 'error') {
            message.error(`${info.file.name} 上传失败`);
        }
    }

    // 校验是否登陆过，有些功能需要先登录
    const checkIsLogin = () => {
        if (loginInfo && loginInfo.token) {
            return true;
        } else {
            Modal.confirm({
                title: '系统提示',
                icon: <ExclamationCircleOutlined />,
                content: '需要先登录才可以使用该功能！',
                okText: '确认',
                cancelText: '取消',
                onOk() {
                    // 打开主应用的登录弹框
                    actions.setGlobalState({ showSign: true });
                },
            });
            return false;
        }
    }

    return (
        <div className="zp-layout">
            {/* <Header title='RESOURCE SHARING BETWEEN  DIFFERENT DEVICES'></Header> */}
            <div className="zp-layout-content share-file-content">
                <div className="operate-line">
                    <div className="operate-line-left">
                        {
                            isHaveSelect.length > 0 ?
                                <div className="operate-btns">
                                    <div className="btn" onClick={() => onShare()}> <i className="iconfont zp-fenxiang"></i> 分享</div>
                                    <div className="btn" onClick={() => onDel()}> <i className="iconfont zp-delete"></i> 删除</div>
                                </div>
                                :
                                <div className="operate-btns" >
                                    {/* key={Math.random()} 解决再次上次文件时清空上次缓存问题   */}
                                    <Upload className="btn" key={Math.random()}
                                        name='file'
                                        action='http://1.15.87.105:11000/minio/love/share/'
                                        showUploadList={false}
                                        multiple={true}
                                        maxCount={6}
                                        onChange={handleChange}>
                                        <div> <i className="iconfont zp-shangchuan"></i> 上传文件</div>
                                    </Upload>
                                    <div className="btn" onClick={() => addFolder()}> <i className="iconfont zp-xinjianwenjianjia"></i> 新建文件夹</div>
                                    <div className="btn" onClick={() => addNote()}> <i className="iconfont zp-newdocument"></i> 新建笔记</div>
                                </div>
                        }
                        <div className="analysis-share" onClick={() => analysisFile()}> 
                            <i className="iconfont zp-yunjiexi"></i> 解析分享码
                        </div>
                    </div>
                    <div className="operate-input">
                        <Input placeholder="搜索我的文件" value={searchInfo} size="middle" 
                            onChange={setSearchVal} onPressEnter={onEnter}/>
                        <span className="search-txt">
                            {
                                searchInfo ? <i className="iconfont zp-fork" onClick={() => clearInfo()}></i> : ''
                            }
                            | 搜索
                        </span> 
                    </div>
                </div>
                <div className="operate-line two-line">
                    {/* <div className="go-back">
                        <i className="iconfont zp-jiantouyou left" 
                            style={{color: fileCatalogueList.length > 1 ? '#000' : '#666'}}
                            onClick={() => goBack()}></i> 
                        <i className="iconfont zp-jiantouyou" onClick={() => goIn()}></i> 
                    </div> */}
                    <div className="catalogues">
                        {
                            fileCatalogueList.map((obj, index) => {
                                return <span className="catalogue" key={index} onClick={() => onClickCatalogue(obj) }>
                                    {obj.txt} <i className="iconfont zp-xiangyoujiantou"></i>
                                </span> 
                            })
                        }
                    </div>
                    <div>
                        {
                            showType === 'thumbnail' ? 
                                <i className="iconfont zp-dasuolvetuliebiao" onClick={() => changeShow('list')}></i>
                                : <i className="iconfont zp-liebiao" onClick={() => changeShow('thumbnail')}></i> 
                        }
                    </div>
                </div>

                {
                    fileList.length === 0 ? 
                    <div className="no-data">暂无数据，请先 <span onClick={() => addFolder()}>创建文件夹</span> </div> :
                    <div className="file-box">
                        {
                        showType === 'thumbnail' ? 
                            // 缩列图显示
                            <div className="file-box" id="file-box">
                                {
                                    fileList.map((file: any, index: any) => {
                                        return <div className={['file-item-thumbnail', currentFile === file.id ? 'file-item-thumbnail-hover' : '', file.selected ? 'file-item-thumbnail-select' : ''].join(' ')} 
                                            key={index} onMouseOver={() => handleMouseOver(file)} onMouseLeave={() => handleMouseLeave(file)}
                                            onClick={() => onLabelClick(file)} onContextMenu={(e) => onRightClickMenu(file.id, e)}>
                                            <i className={['iconfont', 'zp-ziyuan', file.selected ? 'zp-ziyuan-select' : ''].join(' ')}
                                                onClick={() => selectFileChange(file)}></i>
                                            {
                                                file.type === 3 ? 
                                                <img className="abbr-img" src={getPreviewFile('systemId=share&fileId=' + file.fileId)} alt="" />
                                                :
                                                <svg className="icon" aria-hidden="true">
                                                    <use xlinkHref={`#${file.file_icon}`}></use>
                                                </svg>
                                            }
                                            <div className="file-name">
                                                {
                                                    file.rename ? 
                                                        <div className="txt">
                                                            <Input value={file.title} size="middle" id={'input' + file.id}
                                                                onChange={setFileName} onPressEnter={() => onFileEnter(file)}/>
                                                        </div>
                                                        :
                                                        <span className="txt">{file.title}</span>
                                                }
                                            </div>
                                        </div>
                                    })
                                }
                            </div>
                            :
                            // 列表显示
                            <div className="file-box file-box-list">
                                <div className="file-item-list file-box-head">
                                    <Checkbox checked={allSelected} onChange={() => selectAllFile()}></Checkbox>
                                    <div className="file-name">
                                        <span className="txt">文件名</span>
                                    </div>
                                    <div className="file-field file-time">修改时间</div>
                                    <div className="file-field">类型</div>
                                    <div className="file-field">大小</div>
                                </div>
                                <div className="file-box-body">
                                    {
                                        fileList.map((file: any, index: any) => {
                                            return <div className={['file-item-list', currentFile === file.id ? 'list-hover' : '', file.selected ? 'list-select' : ''].join(' ')}  
                                                key={index} onMouseOver={() => handleMouseOver(file)} onMouseLeave={() => handleMouseLeave(file)}
                                                onClick={() => onLabelClick(file)}>
                                                <Checkbox checked={file.selected} onChange={() => selectFileChange(file)}></Checkbox>
                                                <div className="file-name">
                                                    {
                                                        file.type === 3 ? 
                                                        <img className="abbr-img" src={getPreviewFile('systemId=share&fileId=' + file.fileId)} alt="" />
                                                        :
                                                        <svg className="icon" aria-hidden="true">
                                                            <use xlinkHref={`#${file.file_icon}`}></use>
                                                        </svg>
                                                    }
                                                    {
                                                        file.rename ? 
                                                            <div className="txt">
                                                                <Input value={file.title} size="middle" id={'input' + file.id}
                                                                    onChange={setFileName} onPressEnter={() => onFileEnter(file)}/>
                                                            </div>
                                                            :
                                                            <span className="txt">{file.title}</span>
                                                    }
                                                </div>
                                                <div className="file-field file-time">
                                                    {
                                                        currentFile === file.id && !file.selected ?
                                                            <div className="file-operate">
                                                                <i className="iconfont zp-zhongmingming" onClick={() => onReName(file)}></i>
                                                                <i className="iconfont zp-fenxiang" onClick={() => onShare(file)}></i>
                                                                <i className="iconfont zp-delete" onClick={() => onDel(file)}></i>
                                                            </div>
                                                            :
                                                            <div>{dealFormate(file.createTime)}</div>
                                                    }
                                                </div>
                                                <div className="file-field">
                                                    {
                                                        currentFile === file.id && !file.selected ?
                                                            <div></div>
                                                            :
                                                            <div>{file.file_txt}</div>
                                                    }
                                                </div>
                                                <div className="file-field">
                                                    {
                                                        currentFile === file.id && !file.selected ?
                                                            <div></div>
                                                            :
                                                            <div>{file.size}</div>
                                                    }
                                                </div>
                                            </div>
                                        })
                                    }
                                </div>
                            </div>
                        }
                    </div>
                }
                
                <div className="total-count">{fileList.length}项</div>
            </div>

            <Modal title="生成分享码" open={shareModalOpen} 
                footer={[
                    <Button key="back" onClick={shareModalCancel}>
                      关闭
                    </Button>
                ]}>
                <Button type="primary" onClick={generateShareCode}>生成</Button>
                {
                    shareCode ? 
                    <div className="share-code">
                        <span id="sharecode">{shareCode}</span>  
                        <i className="iconfont zp-fuzhi" onClick={() => onCopy}></i>
                    </div>
                    :
                    ''
                }
            </Modal>

            <Modal title="解析分享码" open={codeModalOpen} 
                footer={[
                    <Button key="back" onClick={codeModalCancel}>
                      关闭
                    </Button>
                ]}>
                <div className="analysis-code">
                    <Input placeholder="请输入分享码" value={analysisCode} onChange={onAnalysisCode}/>
                    <Button type="primary" onClick={analysisShareCode}>解析预览</Button>
                    <Button type="primary" onClick={saveShareCodeFile}>保存分享文件</Button>
                </div>
            </Modal>

            <Modal title={currentFileData && currentFileData.id ? '编辑笔记' : '新建笔记'} width="700px" open={noteModalOpen} footer={null}
                onCancel={noteModalCancel}>
                <TextEditor data={currentFileData} catalogue={fileCatalogueList} onNoteModalCancel={noteModalCancel}></TextEditor>
            </Modal>

            {/* 右击显示选项 */}
            <div className="right-menu">
                <ul className="up">{getRightMenuBtns()}</ul>
            </div>
        </div>
    )
}


//一般情况state转props不需要拆分
const mapStateToProps = (state: any) => {
	return {
		loginInfo: state.loginInfo,
	};
};

const ShareFileComponent = connect(mapStateToProps)(ShareFile);
export default ShareFileComponent;


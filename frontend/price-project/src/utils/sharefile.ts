export const sizeTostr = (size: any) => {
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

// 处理时间显示
export const dealFormate = (time: any) => {
    return time.replace('T', ' ');
}

// 处理文件类型转换
export const dealTypeStr = (type: any) => {
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
export const dealUploadType = (type: any) => {
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
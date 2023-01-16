import { Col, message, Row, UploadProps } from 'antd';
import Dragger from 'antd/lib/upload/Dragger';
import React from 'react';
import { InboxOutlined } from '@ant-design/icons';
import '@/pages/goods/phone.less';

const Phone = () => {

    const props: UploadProps = {
        name: 'file',
        multiple: false,
        action: 'https://www.mocky.io/v2/5cc8019d300000980a055e76',
        onChange(info) {
          const { status } = info.file;
          if (status !== 'uploading') {
            console.log(info.file, info.fileList);
          }
          if (status === 'done') {
            message.success(`${info.file.name} file uploaded successfully.`);
          } else if (status === 'error') {
            message.error(`${info.file.name} file upload failed.`);
          }
        },
        onDrop(e) {
          console.log('Dropped files', e.dataTransfer.files);
        },
    };

    return (
        <Row gutter={22}>
            <Col className="gutter-row" span={8} >
                <div className="zp-price-content-left">
                    <Dragger {...props}>
                        <p className="ant-upload-drag-icon">
                        <InboxOutlined />
                        </p>
                        <p className="ant-upload-text">点击或者拖拽图片至此处解析</p>
                    </Dragger>
                </div>
            </Col>
            <Col className="gutter-row" span={16} >
                <div className="zp-price-content-right">
                    <div className='zp-phone-head'>
                        <div className='head-txt'>
                            凑单<br></br>
                            目标
                        </div>
                        <div className='target-price'>18844</div>
                        <div className='head-txt'>
                            已凑<br></br>
                            金额
                        </div>
                        <div className='target-price'>8822</div>
                        <div className='head-txt'>
                            目标<br></br>
                            还差
                        </div>
                        <div className='target-price'>10032</div>
                    </div>
                    <div className='zp-phone-body'>
                        <div className='zp-phone-item'>
                            <div className='zp-phone-item-good'>688</div>
                            <div className='zp-phone-item-name'>香辣鸭舌100g</div>
                        </div>
                        <div className='zp-phone-item'>
                            <div className='zp-phone-item-good'></div>
                            <div className='zp-phone-item-name'>hhh</div>
                        </div>
                        <div className='zp-phone-item'>
                            <div className='zp-phone-item-good'></div>
                            <div className='zp-phone-item-name'>hhh</div>
                        </div>
                        <div className='zp-phone-item'>
                            <div className='zp-phone-item-good'></div>
                            <div className='zp-phone-item-name'>hhh</div>
                        </div>
                        <div className='zp-phone-item'>
                            <div className='zp-phone-item-good'></div>
                            <div className='zp-phone-item-name'>hhh</div>
                        </div>
                        <div className='zp-phone-item'>
                            <div className='zp-phone-item-good'></div>
                            <div className='zp-phone-item-name'>hhh</div>
                        </div>
                        <div className='zp-phone-item'>
                            <div className='zp-phone-item-good'></div>
                            <div className='zp-phone-item-name'>hhh</div>
                        </div>
                        <div className='zp-phone-item'>
                            <div className='zp-phone-item-good'></div>
                            <div className='zp-phone-item-name'>hhh</div>
                        </div>
                        <div className='zp-phone-item'>
                            <div className='zp-phone-item-good'></div>
                            <div className='zp-phone-item-name'>hhh</div>
                        </div>
                        <div className='zp-phone-item'>
                            <div className='zp-phone-item-good'></div>
                            <div className='zp-phone-item-name'>hhh</div>
                        </div>
                    </div>
                </div>
            </Col>
        </Row>
    );
}
export default Phone;
import { useEffect, useRef, useState } from 'react';
import '@/components/textEditor.less';
import { createTxt, updateFile } from '@/api/index';

import ReactQuill, { Quill } from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import quillEmoji from 'quill-emoji';
import "quill-emoji/dist/quill-emoji.css"; //这个不引入的话会出现emoji框一直在输入框下面的情况
import { Button, message } from 'antd';
// import { ImageDrop } from './plugin/quill-image-drop-module'; //讲图片拖进文本框，可以直接安装quill-image-drop-module；但由于我的webpack版本过低，无法兼容es6，所以把插件拿出来了
//注册ToolbarEmoji，将在工具栏出现emoji；注册TextAreaEmoji，将在文本输入框处出现emoji。VideoBlot是我自定义的视频组件，后面会讲，
const { EmojiBlot, ShortNameEmoji, ToolbarEmoji, TextAreaEmoji } = quillEmoji;
Quill.register({
  'formats/emoji': EmojiBlot,
//   'formats/video': VideoBlot,
  'modules/emoji-shortname': ShortNameEmoji,
  'modules/emoji-toolbar': ToolbarEmoji,
  'modules/emoji-textarea': TextAreaEmoji,
  // 'modules/ImageExtend': ImageExtend, //拖拽图片扩展组件
//   'modules/ImageDrop': ImageDrop, //复制粘贴组件
}, true);

const TextEditor = (props: any) => {
    const [reactQuillRef, setReactQuillRef] = useState<any>();
    const [type, setType] = useState('add');
    const newType = useRef(type) as any;
    const [fileContent, setFileContent] = useState('' as any);
    const newFileContent = useRef(fileContent) as any;
    const [catalogue, setCatalogue] = useState([] as any);

    useEffect(() => {
      setCatalogue(props.catalogue);
      if (props.data && props.data.content) {
        newType.current = 'edit';
        setType(newType.current);
        newFileContent.current = props.data;
        setFileContent(newFileContent.current);
      } else {
        newType.current = 'add';
        setType(newType.current);
        newFileContent.current = '';
        setFileContent(newFileContent.current);
      }
      console.log(fileContent, props, type, 'fileContent');
    })

    const onHandleChange = () => {
        const editor = reactQuillRef.getEditor();
        const unprivilegedEditor = reactQuillRef.makeUnprivilegedEditor(editor);
        unprivilegedEditor.getText();

        console.log(unprivilegedEditor.getText());
        console.log(unprivilegedEditor.getHTML());
    }

    const modules = {
        toolbar: [
          [{ 'header': [1, 2, false] }],
          ['bold', 'italic', 'underline','strike', 'blockquote'],
          [{'list': 'ordered'}, {'list': 'bullet'}, {'indent': '-1'}, {'indent': '+1'}],
          [{
            'background': ['rgb(  0,   0,   0)', 'rgb(230,   0,   0)', 'rgb(255, 153,   0)',
              'rgb(255, 255,   0)', 'rgb(  0, 138,   0)', 'rgb(  0, 102, 204)',
              'rgb(153,  51, 255)', 'rgb(255, 255, 255)', 'rgb(250, 204, 204)',
              'rgb(255, 235, 204)', 'rgb(255, 255, 204)', 'rgb(204, 232, 204)',
              'rgb(204, 224, 245)', 'rgb(235, 214, 255)', 'rgb(187, 187, 187)',
              'rgb(240, 102, 102)', 'rgb(255, 194, 102)', 'rgb(255, 255, 102)',
              'rgb(102, 185, 102)', 'rgb(102, 163, 224)', 'rgb(194, 133, 255)',
              'rgb(136, 136, 136)', 'rgb(161,   0,   0)', 'rgb(178, 107,   0)',
              'rgb(178, 178,   0)', 'rgb(  0,  97,   0)', 'rgb(  0,  71, 178)',
              'rgb(107,  36, 178)', 'rgb( 68,  68,  68)', 'rgb( 92,   0,   0)',
              'rgb(102,  61,   0)', 'rgb(102, 102,   0)', 'rgb(  0,  55,   0)',
              'rgb(  0,  41, 102)', 'rgb( 61,  20,  10)']
          }],
          [{
            'color': ['rgb(  0,   0,   0)', 'rgb(230,   0,   0)', 'rgb(255, 153,   0)',
              'rgb(255, 255,   0)', 'rgb(  0, 138,   0)', 'rgb(  0, 102, 204)',
              'rgb(153,  51, 255)', 'rgb(255, 255, 255)', 'rgb(250, 204, 204)',
              'rgb(255, 235, 204)', 'rgb(255, 255, 204)', 'rgb(204, 232, 204)',
              'rgb(204, 224, 245)', 'rgb(235, 214, 255)', 'rgb(187, 187, 187)',
              'rgb(240, 102, 102)', 'rgb(255, 194, 102)', 'rgb(255, 255, 102)',
              'rgb(102, 185, 102)', 'rgb(102, 163, 224)', 'rgb(194, 133, 255)',
              'rgb(136, 136, 136)', 'rgb(161,   0,   0)', 'rgb(178, 107,   0)',
              'rgb(178, 178,   0)', 'rgb(  0,  97,   0)', 'rgb(  0,  71, 178)',
              'rgb(107,  36, 178)', 'rgb( 68,  68,  68)', 'rgb( 92,   0,   0)',
              'rgb(102,  61,   0)', 'rgb(102, 102,   0)', 'rgb(  0,  55,   0)',
              'rgb(  0,  41, 102)', 'rgb( 61,  20,  10)']
          }],
          ['link', 'image'],
          ['clean']
        ],
      }
    
    const formats = [
        'header',
        'bold', 'italic', 'underline', 'strike', 'blockquote',
        'list', 'bullet', 'indent',
        'link', 'image'
    ]

    // 取消
    const onCancel = () => {
        console.log('cancel');
        props.onNoteModalCancel(false);
    }

    // 确认保存数据
    const onSave = async() => {
        const editor = reactQuillRef.getEditor();
        const unprivilegedEditor = reactQuillRef.makeUnprivilegedEditor(editor);
        unprivilegedEditor.getText();

        console.log(unprivilegedEditor.getText());
        console.log(unprivilegedEditor.getHTML());
        props.onNoteModalCancel(false);
        const title = unprivilegedEditor.getText().slice(0, 5);

        if (type === 'edit') {
          const param = {
            id: fileContent ? fileContent.id : '',
            title: title,
            content: unprivilegedEditor.getHTML(),
            fileId: fileContent ? fileContent.fileId : '',
            parentId: catalogue[catalogue.length - 1].id,
            fileType: fileContent ? fileContent.type : '',
          };
          const { status } = await updateFile(param);
          if (status === 200) {
          }
        } else {
          const param = {
            title: title,
            content: unprivilegedEditor.getHTML() + '',
            parentId: catalogue[catalogue.length - 1].id,
          };
          const { status } = await createTxt(param);
          if (status === 200) {
          }
        }
    }

    return (
        <div>
            <ReactQuill theme="snow" ref={(el) => setReactQuillRef(el)}
                value={fileContent.content ? fileContent && fileContent.content : '' } 
                modules={modules}
                formats={formats}
                onBlur={() => onHandleChange()}
                // onChange={(e) => onHandleChange(e)} 
                className="quill-dom"/>
            <div className='footer-btn'>
                <Button onClick={onCancel}>取消</Button>
                <Button type="primary" onClick={onSave}>确定</Button>
            </div>
        </div>
        
    )
}

export default TextEditor;
import '@wangeditor/editor/dist/css/style.css' // 引入 css

import React, { useState, useEffect, useRef } from 'react'
import { Editor, Toolbar } from '@wangeditor/editor-for-react'
import { IDomEditor, IEditorConfig, IToolbarConfig } from '@wangeditor/editor'
import { Button, message } from 'antd';
import { createTxt, updateFile } from '@/api/index';
import '@/components/textEditor.less';

const TextEditor = (props: any) => {
    // editor 实例
    const [editor, setEditor] = useState<IDomEditor | null>(null)   // TS 语法
    // const [editor, setEditor] = useState(null)                   // JS 语法

    // 编辑器内容
    const [type, setType] = useState('add');
    const newType = useRef(type) as any;
    const [fileContent, setFileContent] = useState('' as any);
    const newFileContent = useRef(fileContent) as any;
    const [catalogue, setCatalogue] = useState([] as any);

    // 工具栏配置
    const toolbarConfig: Partial<IToolbarConfig> = {
    }  // TS 语法

    // 编辑器配置
    const editorConfig: Partial<IEditorConfig> = {    // TS 语法
        readOnly: false,
    }

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
        if (editor == null) return
        editor.enable();
        editor.focus(true);
        console.log(props, editor);
    })



    editorConfig.onCreated = (editor: IDomEditor) => {   // TS 语法
        editor.enable();
        editor.focus(true);
        console.log(editor.getConfig(), editor.isDisabled, 'llllll');
    }

    editorConfig.onChange = (editor: IDomEditor) => {   // TS 语法
        console.log('onChange');
    }

    editorConfig.onFocus = (editor: IDomEditor) => {   // TS 语法
        console.log('onFocus');
    }

    // 及时销毁 editor ，重要！
    useEffect(() => {
        return () => {
            if (editor == null) return
            editor.destroy()
            setEditor(null)
        }
    }, [editor])

    // 取消
    const onCancel = () => {
        props.onNoteModalCancel(false);
    }

    // 确认保存数据
    const onSave = async() => {
        if (editor == null) return;
        const title = editor.getText().slice(0, 5);

        if (type === 'edit') {
            const param = {
                id: fileContent ? fileContent.id : '',
                title: fileContent ? fileContent.title : title,
                content: editor.getHtml(),
                fileId: fileContent ? fileContent.fileId : '',
                parentId: catalogue[catalogue.length - 1].id,
                fileType: fileContent ? fileContent.type : '',
            };
            const { status } = await updateFile(param);
            if (status === 200) {
                props.onNoteModalCancel(false);
            }
        } else {
            const param = {
                title: title,
                content: editor.getHtml() + '',
                parentId: catalogue[catalogue.length - 1].id,
            };
            const { status } = await createTxt(param);
            if (status === 200) {
                props.onNoteModalCancel(false);
            }
        }
    }

    return (
        <div className='textEditor'>
            <div className='wang-editor' style={{ border: '1px solid #ccc', zIndex: 100}}>
                <Toolbar
                    editor={editor}
                    defaultConfig={toolbarConfig}
                    mode="default"
                    style={{ borderBottom: '1px solid #ccc' }}
                />
                <Editor
                    defaultConfig={editorConfig}
                    value={fileContent.content ? fileContent && fileContent.content : ''}
                    onCreated={setEditor}
                    mode="default"
                    style={{ height: '300px', overflowY: 'auto' }}
                />

            </div>
            <div className='footer-btn'>
                <Button onClick={onCancel}>取消</Button>
                <Button type="primary" onClick={onSave}>确定</Button>
            </div>
        </div>
    )
}

export default TextEditor;
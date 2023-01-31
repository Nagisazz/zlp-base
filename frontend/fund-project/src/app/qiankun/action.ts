
function emptyAction() {
    // 警告：提示当前使用的是空 Action
    console.warn("Current execute action is empty!");
}

// 我们首先设置一个用于通信的Actions类
class Actions {
    actions: any = {
        onGlobalStateChange: emptyAction,
        setGlobalState: emptyAction
    }
    constructor() {

    }
    // 默认值为空Action

    // 设置actions
    setActions(actions: any) {
        this.actions = actions
    }

    // 映射
    onGlobalStateChange(...args: any) {
        return this.actions.onGlobalStateChange(...args)
    }
    // 映射
    setGlobalState(...args: any) {
        return this.actions.setGlobalState(...args)
    }
}

const actions = new Actions()
export default actions

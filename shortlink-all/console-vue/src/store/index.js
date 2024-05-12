import { createStore } from 'vuex'

// 创建一个新的 store 实例
const store = createStore({
  state() {
    return {
      // domain: 'nurl.ink'
      domain: '127.0.0.1'
    }
  }
})

export default store

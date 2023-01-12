import { createStore } from 'redux';
// reducer层的拆离
import reducer from '@/redux/reducers/platformData';
const store = createStore(reducer);

export default store;
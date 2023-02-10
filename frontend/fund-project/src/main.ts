import './public-path';
import { enableProdMode, NgModuleRef, ReflectiveInjector } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';
import actions from './app/qiankun/action';
import { InfoService } from './app/services/info.service';

if (environment.production) {
  enableProdMode();
}
let injector = ReflectiveInjector.resolveAndCreate([InfoService]);
let infoService = injector.get(InfoService)

let app: void | NgModuleRef<AppModule>;
async function render() {
  app = await platformBrowserDynamic()
    .bootstrapModule(AppModule)
    .catch((err) => console.error(err));
}

if (!(window as any).__POWERED_BY_QIANKUN__) {
  render();
}

export async function bootstrap(props: Object) {
  console.log('bootstrap  angular');
}

export async function mount(props: Object) {
  console.log('mounted angular', InfoService);
  if (props) { // 不能注释，否则子应用接收不到主应用数据
    actions.setActions(props)
  }
  actions.onGlobalStateChange((state: any) => {
      console.log("我是子应用angular，我检测到数据了：", state);
      const ssKey = 'platform.login';
      const infoCache = sessionStorage.getItem(ssKey) ? JSON.parse(sessionStorage.getItem(ssKey) as string) : null;
      if (!infoCache) {
        infoService.changeInfo(state);
      }
      setTimeout(() => {
        if (infoCache && (infoCache.token !== state.token)) {
          infoService.changeInfo(state);
          window.location.reload();
        }
      }, 500)
  }, true);
  render();
}

export async function unmount(props: Object) {
  console.log('angular  unmount');
  // @ts-ignore
  app.destroy();
}

module.exports = {
    'appName': 'RoadPricing-POS',
    'appBoard': '/config/index.js',
    // android 监听全局事件homeBack 如果为true 安卓端需要自行调用router.finish方法来关闭应用
    'androidIsListenHomeBack': 'true',
    'version': {
        'android': '1.0.0',
        'iOS': '1.0.0'
    },
    'page': {
        // 'homePage': '/pages/demo/index.js',
        // mine================
        'homePage': '/pages/RoadPricing-POS/index.js',

        'mediatorPage': '/mediator/index.js',
        'navBarColor': '#0F0',
        'navItemColor': '#ffffff'
    },
    'url': {
        //demo===========
        // 'jsServer': 'http://app.weex-eros.com:8889',
        //mine===========
        'jsServer': 'http://192.168.31.142:8889',

        'image': 'https://lev-inf.benmu-health.com/test/xxx',
        // 'image': 'http://139.198.2.158:8081/api/exit/upload',

        'bundleUpdate': 'http://localhosts:3001/app/check',

        //demo===========
        // 'debugServer': 'ws://app.weex-eros.com:8088/debugProxy/native'
        //mine===========
        'debugServer': 'ws://192.168.31.142:8088/debugProxy/native'
    },
    'zipFolder': {
        'iOS': '/ios/WeexEros/WeexEros',
        'android': '/android/WeexFrameworkWrapper/app/src/main/assets'
    },
    'getui': {
        'enabled': 'false',
        'appId': '',
        'appKey': '',
        'appSecret': ''
    },
    'umeng': {
        'enabled': 'false',
        'iOSAppKey': '',
        'androidAppKey': ''
    },
    'wechat': {
        'enabled': 'false',
        'appId': '',
        'appSecret': ''
    },
    'amap': {
        'enabled': 'true',
        'iOSAppKey': '623c0396a9b879461c971a14baa678fb',
        'androidAppKey': ''
    }
}

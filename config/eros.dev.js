// appBoard，mediator 不建议进行修改 如果修改了 也请对应修改
module.exports = {
    'exports': [
        // appBoard
        'js/config/index.js',
        // mediator
        'js/mediator/index.vue',
        // mine===========
        'js/pages/RoadPricing-POS/index.vue',
        'js/pages/RoadPricing-POS/arrearage/index.vue',
        'js/pages/RoadPricing-POS/arrearageHistory/index.vue',
        'js/pages/RoadPricing-POS/arrearageList/index.vue',
        'js/pages/RoadPricing-POS/arrearageDetail/index.vue',
        'js/pages/RoadPricing-POS/home/index.vue',
        'js/pages/RoadPricing-POS/mine/index.vue',
        'js/pages/RoadPricing-POS/putIn/index.vue',
        'js/pages/RoadPricing-POS/putInFinish/index.vue',
        'js/pages/RoadPricing-POS/putOut/index.vue',
        'js/pages/RoadPricing-POS/putOutFinish/index.vue',
        'js/pages/RoadPricing-POS/setPW/index.vue',
        'js/pages/RoadPricing-POS/payType/index.vue',
        'js/pages/RoadPricing-POS/scan/index.vue',
        'js/pages/RoadPricing-POS/tab/index.vue',
        'js/pages/RoadPricing-POS/identify/index.vue',

        // demo==============
        'js/pages/demo/index.vue',
        'js/pages/demo/lifecycle/index.vue',
        'js/pages/demo/assets/index.vue',
        'js/pages/demo/globalAttr/index.vue',
        'js/pages/demo/inputExtend/index.vue',
        'js/pages/demo/refresh/index.vue',
        'js/pages/demo/storage/index.vue',
        'js/pages/demo/navigator/index.vue',
        'js/pages/demo/tools/index.vue',
        'js/pages/demo/router/index.vue',
        'js/pages/demo/router/home.vue',
        'js/pages/demo/event/a.vue',
        'js/pages/demo/event/b.vue',
        'js/pages/demo/notice/index.vue',
        'js/pages/demo/font/index.vue',
        'js/pages/demo/coms/index.vue',
        'js/pages/demo/image/index.vue',
        'js/pages/demo/bmchart/index.vue',
        'js/pages/demo/bmrichtext/index.vue',
        'js/pages/demo/bmcalendar/index.vue',
        'js/pages/demo/other/waterfall.vue',
        'js/pages/demo/other/weex-ui/tab-page/index.vue',

    ],
    'alias': {
        'Components': 'js/components',
        'Common': 'js/common',
        'Config': 'js/config',
        'Widget': 'js/widget',
        'Pages': 'js/pages',
        'Css': 'js/css',
        'Utils': 'js/utils'
    },
    'eslint': false,
    'diff': {
        'pwd': '/Users/yangmingzhe/Work/opensource/eros-diff-folder',
        'proxy': 'https://app.weex-eros.com/source'
    },
    'server': {
        'path': './',
        'port': 8889
    },
    'mockServer': {
        'port': 52077,
        'mockDir': './dist/mock'
    }
}

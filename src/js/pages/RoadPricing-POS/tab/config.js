
export default {
    // 使用 iconFont 模式的tab title配置
    tabIconFontTitles: [
        {
            title: '首页',
            codePoint: '\ue703'
        },
        {
            title: '欠费缴费',
            codePoint: '\ue6ee'
        },
        {
            title: '我的',
            codePoint: '\ue716',
            badge: ""
        }
    ],
    tabIconFontStyles: {
        bgColor: '#FFFFFF',
        titleColor: '#666666',
        activeTitleColor: '#F37E51',
        activeBgColor: '#FFFFFF',
        isActiveTitleBold: true,
        width: 160,
        height: 120,
        fontSize: 24, //24
        textPaddingLeft: 10,
        textPaddingRight: 10,
        iconFontSize: 60,   //60
        iconFontColor: '#8E8E93',
        activeIconFontColor: '#F37E51',
        iconFontUrl: '../../../../iconfont/iconfont.ttf'
    }
}

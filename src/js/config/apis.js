export default {
    'COMMON.getInfo': '/test/info',
    //登录登出
    "rsa": '/common/rsa',               //ok
    "login": '/common/login',           //ok
    "logout": '/common/logout',         //ok
    //首页
    "main": '/api/main/spsList',        //ok
    "reportLocation": '/api/main/reportLocation',
    //入场
    "enterCarInfo": '/api/entrance/carInfo',            //ok
    "enterConfirm": '/api/entrance/confirm',            //ok
    //出场
    "exitCarInfo": "/api/exit/carInfo",                 //ok
    "exitConfirmExit": '/api/exit/confirmExit',         //ok
    "exitArrearsExit": '/api/exit/arrearsExit',         //ok
    "exitUpload": '/api/exit/upload',                   //ok
    //欠费补缴
    "arrearsDetailInfo": '/api/arrears/detailInfo',     //ok
    "arrearsQueryList": '/api/arrears/queryList',       //ok
    //支付相关
    "cashPay": '/api/pay/cashPay',
    "payConfirm": '/api/pay/confirm',
    "prePay": '/api/pay/prePay',
    //收费员
    "totalChargeInfo": '/api/collector/totalChargeInfo',
    "modifyPwd": '/api/collector/modifyPwd',
    //pay-call-back
    "payCallback": '/pay/callback',
    //打印
    "printEnter": '/api/print/enter',
    "printOrder": '/api/print/order',
    "printExit": '/api/print/exit'
}

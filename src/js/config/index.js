import './push'
import Widget from 'Widget'
import apis from './apis'
import routes from './routes'
import { stringify } from "querystringify"
import md5 from 'js-md5'
import Common from './common'
const storage = weex.requireModule('bmStorage')

new Widget({
    router: {
        /**
         * 路由配置表
         */
        routes
    },
    ajax: {
        //demo===========================
        // baseUrl: 'http://app.weex-eros.com:52077',
        //mine===========================
        baseUrl: 'http://47.100.219.142:8081',

        /**
         * 接口别名
         */
        apis,
        // 接口超时时间
        timeout: 30000,

        /**
         * 请求发送统一拦截器 （可选）
         * options 你请求传入的所有参数和配置
         * next
         */
        //demo==================
        // requestHandler (options, next) {
        //     console.log('request-opts', options)
        //     next()
        // },
        //mine==================
        requestHandler (options, next) {
            if(options.name == "rsa") {
                options.data = stringify(options.data);
                if (typeof options.header !== 'undefined') {
                    options.header['Content-Type'] = 'application/x-www-form-urlencoded'
                }else {
                    options.header = {'Content-Type': 'application/x-www-form-urlencoded'}
                }
            } else {
                if(options.name == "login") {
                    options.data = stringify(options.data);
                    options.header = options.header || {};
                    options.header = {
                        'Content-Type': 'application/x-www-form-urlencoded',
                        "Signature": md5(options.data)
                    }
                }else {
                    const { sessionId, token } = JSON.parse(storage.getDataSync('loginInfo').data);
                    console.log("++++++", storage.getDataSync('loginInfo').data)

                    options.data = options.data || {};
                    options.data.timestamp = new Date().getTime();
                    let sortString = Common.paramsSort(options.data);
                    console.log("sortString===",sortString);
                    options.data = stringify(options.data);
                    console.log("options-data===",options.data);
                    options.header = options.header || {};
                    console.log("token+opstions.data=====", token+options.data);
                    options.header = {
                        'Content-Type': options.header["Content-Type"] || 'application/x-www-form-urlencoded',
                        "Session-Id": sessionId,
                        "Signature": md5(token + sortString)
                    }
                }
            }
            console.log('request-options========', options)
            next()
        },
        /**
         * 请求返回统一拦截器 （可选）
         * options 你请求传入的所有参数和配置
         * resData 服务器端返回的所有数据
         * resolve 请求成功请resolve你得结果，这样请求的.then中的成功回调就能拿到你resolve的数据
         * reject 请求成功请resolve你得结果，这样请求的.then中的失败回调就能拿到你reject的数据
         */
        responseHandler (options, resData, resolve, reject) {
            const { status, errorMsg, data } = resData
            if (status !== 200 || status > 410) {
                console.log(`invoke error status: ${status}`)
                console.log(`invoke error message: ${errorMsg}`)
                reject(resData)
            } else {
                // 自定义请求逻辑
                resolve(data)
            }
        }
    }
})

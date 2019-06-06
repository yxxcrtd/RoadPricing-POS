import JSEncrypt from "./jsencrypt.js";
import Config from './common.js';

export default {
    encrypt: function(deviceId, username, password) {
        //参数排序后再转md5============
        // console.log('公共排序方法===================', Config.paramsSort(obj));
        //字符序列化
        let loginMingWen = `${deviceId}&${username}&${password}`
        //创建加密对象
        let encrypt = new JSEncrypt();
        //设置公钥
        encrypt.setPublicKey(Config.serverPublicKey);
        // 添加需要加密的字符串
        let cryptText = encrypt.encrypt(loginMingWen);
        // 返回加密结果
        return cryptText;
    },

    decrypt: function(cipherText) {
        let encrypt = new JSEncrypt();
        //设置私钥
        encrypt.setPrivateKey(Config.privateKey);
        //添加要解密的文本
        let decryptText = encrypt.decrypt(cipherText);
        //返回解密内容
        return decryptText;
    },
}

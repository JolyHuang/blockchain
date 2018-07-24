import com.sharingif.cube.security.binary.Base64Coder;
import sun.security.rsa.RSAPrivateCrtKeyImpl;
import sun.security.rsa.RSAPrivateKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * TODO
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/23 下午12:30
 */
public class MD5RSASign {

    private static String src = "i look";

    static String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAseinituEKKdnPdP1s+XD09r/J0MpG0iBbmK3QbKCyr3EarRPnizupljN1A6FDDr1b85VofZiKdIpfRjyF4vJW2stSaTudao46As5hBpkxYE+kEJ/jtvBs1PnSXHWukpAYtZljGQJ5/QORC/DT1+wfEUtW1MSYlugv9hokTKnxz9phBd1qe1THHdfNkjRHBQW8MqA58xz7HIP0NCPSnJE0QRULLUE/JziO0RtKkQpWBovfFx0xNEgbdisEqRwXGvrwZD+grwcBVpBF2pCT9C1UmxePixgDrYTMOHGtdExfAX8AuC1zwQw4Z42IfkD/m6uRbaLaC25vgMErwkeqA5OuwIDAQAB";
    static String pruvateKeyStr = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCx6KeK24Qop2c90/Wz5cPT2v8nQykbSIFuYrdBsoLKvcRqtE+eLO6mWM3UDoUMOvVvzlWh9mIp0il9GPIXi8lbay1JpO51qjjoCzmEGmTFgT6QQn+O28GzU+dJcda6SkBi1mWMZAnn9A5EL8NPX7B8RS1bUxJiW6C/2GiRMqfHP2mEF3Wp7VMcd182SNEcFBbwyoDnzHPscg/Q0I9KckTRBFQstQT8nOI7RG0qRClYGi98XHTE0SBt2KwSpHBca+vBkP6CvBwFWkEXakJP0LVSbF4+LGAOthMw4ca10TF8BfwC4LXPBDDhnjYh+QP+bq5FtotoLbm+AwSvCR6oDk67AgMBAAECggEACrIWAH8Z/uexiUII73vMvhvYYx/ihMVjIqPEqKp2uoGWIWQdtUC2SEHmfe0xin6w6rvY4hfR5vm/jMnHJCs/O8PBwT+tJl3JdgnWt15u2IDgpgOZb5k+l+1fZkb1xdVY8Zg6GqqY/dzqTqcgctEp5w6xn1/rJwajL2VvQ+trssq/OVPV7sVu/fuRWOJDnQHrfxunBX+UbhGs37FKCnNL8DHDDerHz+7MaoH6npSmv3h1HeLqrWxkusZ7SWKKipXcWFs7OJjsEFd2NTmDuScr6oBUybdhweyzNDiknYCypdpDyKjFrIUcEJbVWEFDhZ9d56CIFm1NKl4H7pLdCp6dgQKBgQD3IMvDoU7jxzjsDMJU5khSY4IEv1X8oP6BqQOfZrleOFbqB6+aLtNtRxsvAZccQTpLNFbJR7R6DsRyT4XRWF8eWqJ2wk0mKo+3+X+21C1mgzPTAxnU40mBSmQkUQtlc/GZERTaFupapK1YjQVqKQRELZIKaYRbx1KsVh2W9eYP6wKBgQC4S7S+FFsBsZn6R7LlQabUVsJ/uMwbcYUJhLQiU6bQBK9IYPBAyK3ii8lEXC7p/sFyCJrQodFmOwEee0TUvdnuhmgwsmO95PVA0gs7AEIJTtChcvCH90jj9+n1KW4zkLoYbG6pvBQUw2FGo9svWP8baRKW5NdlQya1nJJo7nnYcQKBgANoqGbiarwpm1dmNagGJSk9xiVank0Fy3zuX+Zq/FOYY1qHEJ0H2FLBkFZtpy/0lmFDL49HNe4ZFgydnDLvDSDWDzekJnxISgM3pdNXyeiIFsbqXAjbub/WztrRY17a4pJB8g+wETt+2gVGrLRyLGTkXAdYvUKm1TUbWv0iFzJRAoGBAKJEDgy6UXLJVACYtnvCdUhKZ5AbPqL/LZq22//JVXucVFTO/VqdFvT8DScCX1ApvVtRFjaR85QmaZLUCEOkMUkh0ITn3IgrGoGLLtYxIPOfEHTGhd/BK82Ez9rLMrt2N1QfzMPn1Gp/qJuQ1AD5RAx3VCXFsZz4JhILnKGFL4URAoGBAOSzWfBADiSuPt1QNAST9X1rts0fZPXlKqVJhwI3NlC6MgrsxvkA6Jotexor9pohaMC5kB3v+PoKq3Y4i9BarFz0F9Co+wbFLHXq7SF+ui8WhYMc8c3tqYgAOq31LQubNQFguH2i6LVjJsElhKSfaG/Cf7wq7xfTk4OLTrXzmimr";

    public static void main(String[] args) {
        MD5RSASign();
    }





    public static void MD5RSASign(){

        try {

            Base64Coder base64Coder = new Base64Coder();

            // 生成一对密钥
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");  //获取密钥生成器实例
//            keyPairGenerator.initialize(2048);  // 初始化长度
//            KeyPair keyPair = keyPairGenerator.generateKeyPair();
//            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();//生成公钥
//            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();  // 生成私钥
//
//            String publicKeyStr = base64Coder.encode(rsaPublicKey.getEncoded());
//            String pruvateKeyStr = base64Coder.encode(rsaPrivateKey.getEncoded());
//            System.out.println(publicKeyStr);
//            System.out.println(pruvateKeyStr);


            RSAPublicKey rsaPublicKey = new RSAPublicKeyImpl(base64Coder.decode(publicKeyStr));
            RSAPrivateKey rsaPrivateKey = RSAPrivateCrtKeyImpl.newKey(base64Coder.decode(pruvateKeyStr));

            //用私钥进行签名
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());  //私钥转换成pkcs8格式
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec); // 用key工厂对象生成私钥
            Signature signature = Signature.getInstance("MD5withRSA");  //  md5 RSA签名对象
            signature.initSign(privateKey);  //初始化签名
            signature.update(src.getBytes());
            byte[] result = signature.sign();  //对消息进行签名
            System.out.println("签名结果："+result);


            //用公钥进行验证
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            signature.initVerify(publicKey);
            signature.update(src.getBytes());
            boolean verify = signature.verify(result);
            System.out.println("验证结果:"+verify);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

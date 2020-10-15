package com.leyou.auth.test;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String pubKeyPath = "D:\\tmp\\rsa\\rsa.pub";

    private static final String priKeyPath = "D:\\tmp\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void test(){
        String s = "D:\\tmp\\rsa\\rsa.pub";
        int  i = s.lastIndexOf("\\");
        System.out.println(s.substring(0, i));
    }

    @Test
    public void testRsa() throws Exception {
        File file = new File("D:\\tmp\\rsa");
        if(!file.exists()){
            file.mkdirs();
        }
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTYwMjczMDgwMH0.IssDnluXZPrds0SmUF9yfsnw3KrP5imxU2gG2ozSoKNxt8PkWQhpJOONFKY9_AKm6Zzn84GQ7IYtCggvsMFWPW7RN_AOtZBbiWLVfB-Mt3sdmv02-LGtGF911_-jR0N8ecMi1suN7FLHnoa3FWvai2Fx0oGzMh_-h4hyhwYXcbU";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}
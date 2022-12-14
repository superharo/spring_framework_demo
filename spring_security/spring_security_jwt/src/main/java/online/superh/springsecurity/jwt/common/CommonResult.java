package online.superh.springsecurity.jwt.common;

import java.io.Serializable;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-14 13:34
 */
public class CommonResult implements Serializable {

    private String status;
    private String msg;
    private Object result;
    private String jwtToken;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

}

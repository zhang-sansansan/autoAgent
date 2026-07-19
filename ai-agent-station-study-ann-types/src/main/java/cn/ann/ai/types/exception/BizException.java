package cn.ann.ai.types.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 涓氬姟寮傚父
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/9/2 07:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 5317680961212299217L;

    /** 寮傚父鐮?*/
    private String code;

    /** 寮傚父淇℃伅 */
    private String info;

    public BizException(String code) {
        this.code = code;
    }

    public BizException(String code, Throwable cause) {
        this.code = code;
        super.initCause(cause);
    }

    public BizException(String code, String message) {
        this.code = code;
        this.info = message;
    }

    public BizException(String code, String message, Throwable cause) {
        this.code = code;
        this.info = message;
        super.initCause(cause);
    }

    @Override
    public String toString() {
        return "cn.bugstack.ai.types.exception.BizException{" +
                "code='" + code + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
    
}


package cn.ann.ai.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "鎴愬姛"),
    UN_ERROR("0001", "鏈煡澶辫触"),
    ILLEGAL_PARAMETER("0002", "闈炴硶鍙傛暟"),
    LOGIN_FAILED("0003", "鐧诲綍澶辫触"),
    ;

    private String code;
    private String info;

}


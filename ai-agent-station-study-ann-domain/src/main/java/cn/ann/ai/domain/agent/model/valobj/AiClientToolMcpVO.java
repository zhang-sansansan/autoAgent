package cn.ann.ai.domain.agent.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * MCP瀹㈡埛绔厤缃紝鍊煎璞? *
 * @author xiaofuge bugstack.cn @灏忓倕鍝? * 2025/6/27 18:29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientToolMcpVO {

    /**
     * MCP ID
     */
    private String mcpId;

    /**
     * MCP鍚嶇О
     */
    private String mcpName;

    /**
     * 浼犺緭绫诲瀷(sse/stdio)
     */
    private String transportType;

    /**
     * 浼犺緭閰嶇疆(sse/stdio)
     */
    private String transportConfig;

    /**
     * 璇锋眰瓒呮椂鏃堕棿(鍒嗛挓)
     */
    private Integer requestTimeout;

    /**
     * 浼犺緭閰嶇疆 - sse
     */
    private TransportConfigSse transportConfigSse;

    /**
     * 浼犺緭閰嶇疆 - stdio
     */
    private TransportConfigStdio transportConfigStdio;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TransportConfigSse {//闈欐€佸唴閮ㄧ被锛屽ぇ绫讳笓闂ㄤ负灏忕被鏈嶅姟  鏂逛究JSON鏄犲皠
        // 鍔犱簡static涔嬪悗鍙互涓嶇敤棰勫厛鍒涘缓澶х被锛屽彲浠ョ洿鎺ュ垱寤哄唴閮ㄧ被
        private String baseUri;//杩滅▼鏈嶅姟鍣ㄧ殑鍦板潃
        private String sseEndpoint;//绔偣锛?    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TransportConfigStdio {

        private Map<String, Stdio> stdio;//澶氬伐鍏烽厤缃?
        @Data
        public static class Stdio {//Stdio锛氭弿杩板叿浣撴€庝箞杩愯涓€涓懡浠?            private String command;
            private List<String> args;
            private Map<String, String> env;
        }
    }

}


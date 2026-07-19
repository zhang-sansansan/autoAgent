package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientApiResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String apiId;

    private String baseUrl;

    private String apiKey;

    private String completionsPath;

    private String embeddingsPath;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}


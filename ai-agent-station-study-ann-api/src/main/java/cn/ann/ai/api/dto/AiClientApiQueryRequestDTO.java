package cn.ann.ai.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiClientApiQueryRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String apiId;

    private String baseUrl;

    private Integer status;

}

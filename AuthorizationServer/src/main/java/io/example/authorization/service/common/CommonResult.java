package io.example.authorization.service.common;

import io.example.authorization.domain.response.common.Error;
import io.example.authorization.domain.response.common.ProcessingResult;

public class CommonResult {

    /**
     * ServerError 결과 반환
     * @param e
     * @return
     */
    public static ProcessingResult serverError(Exception e){
        return new ProcessingResult(Error.builder()
                .code(500)
                .message("요청을 처리하는 과정에서 오류가 발생하였습니다. 관리자에게 문의하세요.")
                .detail(e.getMessage())
                .build()
        );
    }

    /**
     * notFound 결과 반환
     * @return
     */
    private ProcessingResult notFound(){
        return new ProcessingResult(Error.builder()
                .code(404)
                .message("요청에 해당하는 데이터가 존재하지 않습니다.")
                .build()
        );
    }
}
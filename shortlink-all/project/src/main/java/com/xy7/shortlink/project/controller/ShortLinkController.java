package com.xy7.shortlink.project.controller;

import com.xy7.shortlink.project.common.convention.result.Result;
import com.xy7.shortlink.project.common.convention.result.Results;
import com.xy7.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.xy7.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.xy7.shortlink.project.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接控制层
 */
@RestController
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    /**
     * 创建短链接
     */
    @PostMapping("/api/short-link/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return Results.success(shortLinkService.createShortLink(requestParam));
    }
}

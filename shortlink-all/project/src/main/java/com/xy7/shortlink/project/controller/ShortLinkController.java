package com.xy7.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xy7.shortlink.project.common.convention.result.Result;
import com.xy7.shortlink.project.common.convention.result.Results;
import com.xy7.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.xy7.shortlink.project.dto.req.ShortLinkGroupCountQueryRespDTO;
import com.xy7.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.xy7.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.xy7.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.xy7.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.xy7.shortlink.project.service.ShortLinkService;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/short-link/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return Results.success(shortLinkService.pageShortLink(requestParam));
    }

    /**
     * 查询短链接分组内数量
     */
    @GetMapping("/api/short-link/v1/count")
    public Result<List<ShortLinkGroupCountQueryRespDTO>> listGroupShortLinkCount(@RequestParam("requestParam") List<String> requestParam) {
        return Results.success(shortLinkService.listGroupShortLinkCount(requestParam));
    }

    @PostMapping("/api/short-link/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkService.updateShortLink(requestParam);
        return Results.success();
    }

    @GetMapping("/{short-uri}")
    public void restoreUrl(@PathVariable("short-uri") String shortUri, ServletRequest request, ServletResponse response) {
        shortLinkService.restoreUrl(shortUri, request, response);
    }
}

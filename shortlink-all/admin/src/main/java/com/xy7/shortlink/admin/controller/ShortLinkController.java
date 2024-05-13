package com.xy7.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xy7.shortlink.admin.common.convention.result.Result;
import com.xy7.shortlink.admin.common.convention.result.Results;
import com.xy7.shortlink.admin.remote.dto.ShortLinkActualRemoteService;
import com.xy7.shortlink.admin.remote.dto.ShortLinkRemoteService;
import com.xy7.shortlink.admin.remote.dto.req.ShortLinkBatchCreateReqDTO;
import com.xy7.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.xy7.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.xy7.shortlink.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import com.xy7.shortlink.admin.remote.dto.resp.ShortLinkBaseInfoRespDTO;
import com.xy7.shortlink.admin.remote.dto.resp.ShortLinkBatchCreateRespDTO;
import com.xy7.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.xy7.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.xy7.shortlink.admin.toolkit.EasyExcelWebUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接后管控制层
 */
@RestController
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkActualRemoteService shortLinkActualRemoteService;

    /**
     * 创建短链接
     */
    @PostMapping("/api/short-link/admin/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return shortLinkActualRemoteService.createShortLink(requestParam);
    }

    /**
     * 批量创建短链接
     */
    @SneakyThrows
    @PostMapping("/api/short-link/admin/v1/create/batch")
    public void batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam, HttpServletResponse response) {
        Result<ShortLinkBatchCreateRespDTO> shortLinkBatchCreateRespDTOResult = shortLinkActualRemoteService.batchCreateShortLink(requestParam);
        if (shortLinkBatchCreateRespDTOResult.isSuccess()) {
            List<ShortLinkBaseInfoRespDTO> baseLinkInfos = shortLinkBatchCreateRespDTOResult.getData().getBaseLinkInfos();
            EasyExcelWebUtil.write(response, "批量创建短链接-SaaS短链接系统", ShortLinkBaseInfoRespDTO.class, baseLinkInfos);
        }
    }

    /**
     * 修改短链接
     */
    @PostMapping("/api/short-link/admin/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkActualRemoteService.updateShortLink(requestParam);
        return Results.success();
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/short-link/admin/v1/page")
    public Result<Page<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return shortLinkActualRemoteService.pageShortLink(requestParam.getGid(), requestParam.getOrderTag(), requestParam.getCurrent(), requestParam.getSize());
    }
}
package com.project.service.weixin.material;

import com.alibaba.fastjson.JSON;
import com.project.common.exception.BusinessException;
import com.project.common.exception.ExceptionEnum;
import com.project.common.network.NetUtil;
import com.project.model.material.Request;
import com.project.model.material.Response;
import com.project.service.weixin.access.WechatAccessService;
import com.project.service.weixin.share.ShareService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by goforit on 2017/11/27.
 */
@Service
public class WechatMaterialService extends WechatAccessService {

    @Autowired
    private WechatMaterialProperty wechatMaterialProperty;
    @Autowired
    private ShareService shareService;

    /**
     * 批量获取素材
     * @param request
     * @return
     * @throws BusinessException
     */
    public Response batchget_material(Request request) throws BusinessException {
        String access_token = getToken();
        try {
            String httpContent = NetUtil.getHttpContent(
                    String.format(wechatMaterialProperty.getBatchget_material(), access_token),
                    JSON.toJSONString(request),
                    wechatAccessProperty.getCharset(),
                    wechatAccessProperty.getCharset());

            logger.debug("批量获取素材返回内容：" + httpContent);
            Response response = JSON.parseObject(httpContent, Response.class);
            return response;
        } catch (Exception e) {
            throw new BusinessException(ExceptionEnum.WECHAT_REQUEST_ERROR, e.getMessage(), e);
        }
    }

    /**
     * 批量获取素材的中的文章url
     * @param qrUrl
     * @param request
     * @return
     * @throws BusinessException
     */
    public Map<String, String> batchgetMaterialUrl(String qrUrl, Request request) throws BusinessException {
        Response response = batchget_material(request);
        final Map<String, String> map = new HashedMap();
        response.getItem().forEach((item) -> {
            item.getContent().getNews_item().forEach((news) -> {
                map.put(news.getTitle(), shareService.createShareLink(news.getUrl(), qrUrl));

            });
        });
        return map;
    }

    /**
     * 文章的显示封装
     * @param url
     * @return
     */
    public String showContent(String url){
        return shareService.addProxyUrl(shareService.removeData_src(shareService.removeScript(url)));
    }

    /**
     * 获取真正的图片字节流
     */
    public byte[] getWechatUrl(String key) throws IOException {
        String wechatUrl = shareService.getRealWechatUrl(key);
        URL url = new URL(wechatUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String headerField = connection.getHeaderField("Content-Type");
        logger.info("Content-Type" + headerField);
        DataInputStream in = new DataInputStream(connection.getInputStream());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = in.read(buffer)) != -1){
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        in.close();
        return outputStream.toByteArray();
    }
}
